package org.ikora;

import org.sonar.api.Plugin;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

public final class IkoraPlugin implements Plugin {
    public static final String FILE_SUFFIXES_KEY = "sonar.ikora.file.suffixes";

    @Override
    public void define(Context context) {
        context.addExtensions(
                PropertyDefinition.builder(IkoraPlugin.FILE_SUFFIXES_KEY)
                        .name("File suffixes")
                        .description("Comma-separated list of suffixes for files to analyze.")
                        .defaultValue(".robot")
                        .multiValues(true)
                        .category("Ikora")
                        .onQualifiers(Qualifiers.PROJECT)
                        .build(),
                IkoraLanguage.class,
                IkoraRulesDefinition.class,
                IkoraQualityProfile.class,
                IkoraSensor.class
        );
    }
}
