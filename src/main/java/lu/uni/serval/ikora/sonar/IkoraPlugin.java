package lu.uni.serval.ikora.sonar;

/*-
 * #%L
 * sonar-ikora-plugin
 * %%
 * Copyright (C) 2020 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
