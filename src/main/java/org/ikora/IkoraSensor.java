package org.ikora;

import org.ikora.checks.CheckRepository;
import org.ikora.checks.IkoraCheck;
import org.ikora.checks.IkoraIssue;
import org.ikora.error.Errors;
import org.ikora.model.Project;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
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
import org.ikora.builder.BuildResult;

import org.ikora.builder.Builder;
import org.ikora.model.SourceFile;

import java.util.ArrayList;
import java.util.List;

public class IkoraSensor implements Sensor {
    private static final Logger LOG = Loggers.get(IkoraSensor.class);

    private final Checks<Object> checks;
    private final FileSystem fileSystem;
    private final FilePredicate mainFilesPredicate;
    private final FileLinesContextFactory fileLinesContextFactory;

    public IkoraSensor(FileSystem fileSystem, CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory) {
        this.fileSystem = fileSystem;
        this.fileLinesContextFactory = fileLinesContextFactory;

        this.checks = checkFactory.create(IkoraLanguage.REPOSITORY_KEY).addAnnotatedChecks(
                (Iterable<?>) CheckRepository.getCheckClasses()
        );

        this.mainFilesPredicate = fileSystem.predicates().and(
            fileSystem.predicates().hasType(InputFile.Type.MAIN),
            fileSystem.predicates().hasLanguage(IkoraLanguage.KEY)
        );
    }


    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor
                .onlyOnLanguage(IkoraLanguage.KEY)
                .name("Robot Framework Sensor");
    }

    @Override
    public void execute(SensorContext context) {
        List<InputFile> inputFiles = new ArrayList<>();
        fileSystem.inputFiles(mainFilesPredicate).forEach(inputFiles::add);

        if (inputFiles.isEmpty()) {
            return;
        }

        final BuildResult result = buildProject(context);

        for(InputFile inputFile: inputFiles){
            SourceFile sourceFile = result.getSourceFile(inputFile.uri());

            if(sourceFile == null) {
                LOG.warn(String.format("Skipped file: %s", inputFile.uri().toString()));
                continue;
            }

            LOG.info(String.format("Analyzing file: %s", inputFile.filename()));

            Errors errors = result.getErrors().in(sourceFile.getFile());
            IkoraSourceCode sourceCode = new IkoraSourceCode(inputFile, sourceFile, errors);

            computeLineMeasures(context, sourceCode);
            saveSyntaxHighlighting(context, sourceCode);
            runChecks(context, sourceCode);
        }
    }

    private BuildResult buildProject(SensorContext context) {
        LOG.info("Start building projects...");

        final BuildResult result = Builder.build(fileSystem.baseDir(), new Configuration(), true);

        LOG.info(String.format(
                "Built in %d ms [parsing: %d ms; linking: %d ms]",
                result.getBuildTime(),
                result.getParsingTime(),
                result.getLinkingTime()
        ));

        if(!result.getErrors().isEmpty()){
            LOG.warn("Detected errors while building");
        }
        return result;
    }

    private void computeLineMeasures(SensorContext context, IkoraSourceCode sourceCode) {
        LineCounter.analyse(context, fileLinesContextFactory, sourceCode);
    }

    private void saveSyntaxHighlighting(SensorContext context, IkoraSourceCode sourceCode) {
        //TODO: generate highlights
    }

    private void runChecks(SensorContext context, IkoraSourceCode sourceCode){
        LOG.info(String.format("Number of checks: %d", checks.all().size()));

        for(Object object: checks.all()){
            IkoraCheck check = (IkoraCheck)object;

            check.setRuleKey(checks.ruleKey(check));
            check.setIkoraSourceCode(sourceCode);

            LOG.debug(String.format("Checking rule: %s", check.getRuleKey()));
            check.validate();
        }

        saveIssues(context, sourceCode);
    }

    private void saveIssues(SensorContext context, IkoraSourceCode sourceCode){
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