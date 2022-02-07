package lu.uni.serval.ikora.sonar;

/*-
 * #%L
 * sonar-ikora-plugin
 * %%
 * Copyright (C) 2020 - 2021 University of Luxembourg
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.sonar.api.Plugin;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

public final class IkoraPlugin implements Plugin {
    public static final String CATEGORY = "Ikora";
    public static final String SUBCATEGORY = "General";

    @Override
    public void define(Context context) {
        context.addExtensions(
                PropertyDefinition.builder(IkoraLanguage.FILE_SUFFIXES_KEY)
                        .name("File suffixes")
                        .description("Comma-separated list of suffixes for files to analyze.")
                        .defaultValue(".robot")
                        .multiValues(true)
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .onQualifiers(Qualifiers.PROJECT)
                        .build(),
                PropertyDefinition.builder(IkoraLanguage.MAXIMUM_NUMBER_ARGS)
                        .name("Maximum arguments")
                        .description("Maximum number of arguments that a keyword can have.")
                        .type(PropertyType.INTEGER)
                        .defaultValue("4")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .onQualifiers(Qualifiers.PROJECT)
                        .build(),
                PropertyDefinition.builder(IkoraLanguage.MAXIMUM_NUMBER_TEST_STEPS)
                        .name("Maximum test steps")
                        .description("Maximum number of steps acceptable for a test.")
                        .type(PropertyType.INTEGER)
                        .defaultValue("10")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .onQualifiers(Qualifiers.PROJECT)
                        .build(),
                PropertyDefinition.builder(IkoraLanguage.MINIMUM_NUMBER_TEST_STEPS)
                        .name("Minimum test steps")
                        .description("Minimum number of steps acceptable for a test.")
                        .type(PropertyType.INTEGER)
                        .defaultValue("3")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
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
                IkoraQualityProfile.class,
                IkoraSensor.class
        );
    }
}
