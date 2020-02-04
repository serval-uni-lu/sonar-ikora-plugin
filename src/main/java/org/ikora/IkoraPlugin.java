package org.ikora;

import org.sonar.api.Plugin;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

public final class IkoraPlugin implements Plugin {

    @Override
    public void define(Context context) {
        context.addExtensions(
                PropertyDefinition.builder(IkoraLanguage.FILE_SUFFIXES_KEY)
                        .name("File suffixes")
                        .description("Comma-separated list of suffixes for files to analyze.")
                        .defaultValue(".robot")
                        .multiValues(true)
                        .category("Ikora")
                        .subCategory("General")
                        .onQualifiers(Qualifiers.PROJECT)
                        .build(),
                PropertyDefinition.builder(IkoraLanguage.MAXIMUM_NUMBER_ARGS)
                        .name("Maximum arguments")
                        .description("Maximum number of arguments that a keyword can have.")
                        .type(PropertyType.INTEGER)
                        .defaultValue("4")
                        .category("Ikora")
                        .subCategory("General")
                        .onQualifiers(Qualifiers.PROJECT)
                        .build(),
                PropertyDefinition.builder(IkoraLanguage.MAXIMUM_NUMBER_TEST_STEPS)
                        .name("Minimum test steps")
                        .description("Minimum number of steps acceptable for a test.")
                        .type(PropertyType.INTEGER)
                        .defaultValue("3")
                        .category("Ikora")
                        .subCategory("General")
                        .onQualifiers(Qualifiers.PROJECT)
                        .build(),
                PropertyDefinition.builder("sonar.cpd." + IkoraLanguage.KEY + ".minimumLines")
                        .type(PropertyType.INTEGER)
                        .defaultValue("3")
                        .onQualifiers(Qualifiers.PROJECT)
                        .build(),
                PropertyDefinition.builder("sonar.cpd." + IkoraLanguage.KEY + ".minimumTokens")
                        .type(PropertyType.INTEGER)
                        .defaultValue("5")
                        .onQualifiers(Qualifiers.PROJECT)
                        .build(),
                IkoraLanguage.class,
                IkoraRulesDefinition.class,
                IkoraQualityProfile.class,
                IkoraSensor.class
        );
    }
}
