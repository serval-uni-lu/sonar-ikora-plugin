package org.ukwikora;

import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.ukwikora.checks.ParsingErrorCheck;

import org.ukwikora.builder.Builder;
import org.ukwikora.model.Project;
import org.ukwikora.model.SourceFile;

import java.util.ArrayList;
import java.util.List;

public class UkwikoraSensor implements Sensor {
    private static final Logger LOG = Loggers.get(UkwikoraSensor.class);
    private static final RuleKey PARSING_ERROR_RULE_KEY = RuleKey.of(UkwikoraLanguage.REPOSITORY_KEY, ParsingErrorCheck.RULE_KEY);

    private final Checks<Object> checks;
    private final FileSystem fileSystem;
    private final FilePredicate mainFilesPredicate;
    private final FileLinesContextFactory fileLinesContextFactory;

    public UkwikoraSensor(FileSystem fileSystem, CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory) {
        this.fileSystem = fileSystem;
        this.fileLinesContextFactory = fileLinesContextFactory;

        this.checks = checkFactory.create(UkwikoraLanguage.KEY);

        this.mainFilesPredicate = fileSystem.predicates().and(
            fileSystem.predicates().hasType(InputFile.Type.MAIN),
            fileSystem.predicates().hasLanguage(UkwikoraLanguage.KEY)
        );
    }


    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor
                .onlyOnLanguage(UkwikoraLanguage.KEY)
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
        Builder builder = new Builder();
        Project project = builder.build(fileSystem.baseDir(), true);

        LOG.info(String.format(
                "Built in %d ms [parsing: %d ms; linking: %d ms]",
                builder.getBuildTime(),
                builder.getParsingTime(),
                builder.getLinkingTime()
        ));

        if(!builder.getErrors().isEmpty()){
            LOG.warn(String.format("Detected %d errors while building", builder.getErrors().getSize()));
        }

        for(InputFile inputFile: inputFiles){
            SourceFile sourceFile = project.getSourceFile(inputFile.uri());
            LineCounter.analyse(context, fileLinesContextFactory, sourceFile, inputFile);
        }
    }
}
