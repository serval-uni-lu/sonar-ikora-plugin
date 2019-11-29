package org.ukwikora;

import org.sonar.api.SonarProduct;
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
                .name("XML Sensor");
    }

    @Override
    public void execute(SensorContext context) {
        List<InputFile> inputFiles = new ArrayList<>();
        fileSystem.inputFiles(mainFilesPredicate).forEach(inputFiles::add);

        if (inputFiles.isEmpty()) {
            return;
        }
    }
}
