package org.ikora;

import org.ikora.checks.CheckRepository;
import org.ikora.checks.IkoraCheck;
import org.ikora.checks.ParsingErrorCheck;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
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

        LOG.info("Start building projects...");
        final BuildResult result = Builder.build(fileSystem.baseDir(), true);

        LOG.info(String.format(
                "Built in %d ms [parsing: %d ms; linking: %d ms]",
                result.getBuildTime(),
                result.getParsingTime(),
                result.getLinkingTime()
        ));

        if(!result.getErrors().isEmpty()){
            LOG.warn(String.format("Detected %d errors while building", result.getErrors().getSize()));
        }

        for(InputFile inputFile: inputFiles){
            SourceFile sourceFile = result.getSourceFile(inputFile.uri());

            if(sourceFile == null) {
                LOG.warn(String.format("Skipped file: %s", inputFile.filename()));
                continue;
            }

            LOG.debug(String.format("Analyzing file: %s", inputFile.filename()));

            IkoraSourceCode sourceCode = new IkoraSourceCode(inputFile, sourceFile);

            computeLineMeasures(context, sourceCode);
        }
    }

    private void computeLineMeasures(SensorContext context, IkoraSourceCode sourceCode) {
        LineCounter.analyse(context, fileLinesContextFactory, sourceCode);
    }

    private void runChecks(SensorContext context, IkoraSourceCode sourceCode){
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
        //TODO: save the issues
    }
}
