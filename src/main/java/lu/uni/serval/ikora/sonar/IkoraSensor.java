package lu.uni.serval.ikora.sonar;

import lu.uni.serval.ikora.core.BuildConfiguration;
import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.error.Errors;
import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.sonar.checks.CheckRepository;
import lu.uni.serval.ikora.sonar.checks.IkoraCheck;
import lu.uni.serval.ikora.sonar.checks.IkoraIssue;
import lu.uni.serval.ikora.sonar.metrics.CpdAnalyzer;
import lu.uni.serval.ikora.sonar.metrics.FunctionAnalyzer;
import lu.uni.serval.ikora.sonar.metrics.LineAnalyzer;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.ArrayList;
import java.util.List;

public class IkoraSensor implements Sensor {
    private static final Logger LOG = Loggers.get(IkoraSensor.class);

    private final Checks<Object> checks;
    private final FileLinesContextFactory fileLinesContextFactory;

    public IkoraSensor(CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory) {
        this.fileLinesContextFactory = fileLinesContextFactory;

        this.checks = checkFactory
                .create(IkoraLanguage.REPOSITORY_KEY)
                .addAnnotatedChecks(CheckRepository.getCheckClasses());
    }


    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor
                .onlyOnLanguage(IkoraLanguage.KEY)
                .name("Robot Framework Sensor");
    }

    @Override
    public void execute(SensorContext context) {
        List<InputFile> inputFiles = getInputFiles(context);
        if (inputFiles.isEmpty()) return;

        final BuildResult result = buildProject(context);

        for(InputFile inputFile: inputFiles){
            SourceFile sourceFile = result.getSourceFile(inputFile.uri());

            if(sourceFile == null) {
                LOG.warn(String.format("Skipped file: %s", inputFile.uri().toString()));
                continue;
            }

            LOG.info(String.format("Analyzing file: %s", inputFile.filename()));

            Errors errors = result.getErrors().in(sourceFile.getSource());
            IkoraSourceCode sourceCode = new IkoraSourceCode(inputFile, sourceFile, errors);

            computeMetrics(context, sourceCode);
            saveSyntaxHighlighting(context, sourceCode);
            runChecks(context, sourceCode);
        }
    }

    private List<InputFile> getInputFiles(SensorContext context) {
        List<InputFile> inputFiles = new ArrayList<>();

        FilePredicate mainFilesPredicate = context.fileSystem().predicates().and(
                context.fileSystem().predicates().hasType(InputFile.Type.MAIN),
                context.fileSystem().predicates().hasLanguage(IkoraLanguage.KEY)
        );

        context.fileSystem().inputFiles(mainFilesPredicate).forEach(inputFiles::add);

        return inputFiles;
    }

    private BuildResult buildProject(SensorContext context) {
        LOG.info("Start building projects...");

        final BuildResult result = Builder.build(context.fileSystem().baseDir(), new BuildConfiguration(), true);

        LOG.info(String.format(
                "Built projects in %d ms [parsing: %d ms; linking: %d ms]",
                result.getBuildTime(),
                result.getParsingTime(),
                result.getResolveTime()
        ));

        return result;
    }

    private void computeMetrics(SensorContext context, IkoraSourceCode sourceCode) {
        LineAnalyzer.analyse(context, fileLinesContextFactory, sourceCode);
        FunctionAnalyzer.analyse(context, sourceCode);
        CpdAnalyzer.analyse(context, sourceCode);
    }

    private void saveSyntaxHighlighting(SensorContext context, IkoraSourceCode sourceCode) {
        //TODO: generate highlights
    }

    private void runChecks(SensorContext context, IkoraSourceCode sourceCode){
        for(Object object: checks.all()){
            IkoraCheck check = (IkoraCheck)object;

            check.setRuleKey(checks.ruleKey(check));
            check.setIkoraSourceCode(sourceCode);
            check.setContext(context);

            LOG.debug(String.format("Checking rule: %s", check.getRuleKey()));
            check.validate();
        }

        saveIssues(context, sourceCode);
    }

    private void saveIssues(SensorContext context, IkoraSourceCode sourceCode){
        LOG.debug(String.format("Issues found: %d", sourceCode.getIkoraIssues().size()));

        for(IkoraIssue issue: sourceCode.getIkoraIssues()){
            LOG.debug(String.format("Save issue: %s", issue.getMessage()));

            NewIssue newIssue = context.newIssue().forRule(issue.getRuleKey());
            NewIssueLocation location = newIssue.newLocation()
                    .on(sourceCode.getInputFile())
                    .message(issue.getMessage())
                    .at(sourceCode.getInputFile().selectLine(issue.getLine() == 0 ? 1 : issue.getLine()));

            newIssue.at(location).save();
        }
    }
}
