package org.ukwikora;

import org.sonar.api.Plugin;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

public final class UkwikoraPlugin implements Plugin {
    public static final String FILE_SUFFIXES_KEY = "sonar.ukwikora.file.suffixes";

    @Override
    public void define(Context context) {
        context.addExtensions(
                PropertyDefinition.builder(UkwikoraPlugin.FILE_SUFFIXES_KEY)
                        .name("File suffixes")
                        .description("Comma-separated list of suffixes for files to analyze.")
                        .defaultValue(".robot")
                        .multiValues(true)
                        .category("Ukwikora")
                        .onQualifiers(Qualifiers.PROJECT)
                        .build(),
                UkwikoraLanguage.class,
                UkwikoraRulesDefinition.class,
                UkwikoraQualityProfile.class,
                UkwikoraSensor.class
        );
    }
}
