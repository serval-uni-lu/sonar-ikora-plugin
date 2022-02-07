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

import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;

import java.util.ArrayList;
import java.util.List;

public class IkoraLanguage extends AbstractLanguage {

    public static final String KEY = "ikora";
    public static final String REPOSITORY_KEY = "ikora";
    public static final String REPOSITORY_NAME = "SonarAnalyzer";

    public static final String FILE_SUFFIXES_KEY = "sonar.ikora.file.suffixes";
    public static final String MAXIMUM_NUMBER_ARGS = "sonar.ikora.max.args";
    public static final String MAXIMUM_NUMBER_TEST_STEPS = "sonar.ikora.maximum.test.steps";
    public static final String MINIMUM_NUMBER_TEST_STEPS = "sonar.ikora.minimum.test.steps";

    private static final String[] DEFAULT_SUFFIXES = {".robot"};
    private static final String IKORA_LANGUAGE_NAME = "Robot Framework";

    public static final String IKORA_RESOURCE_PATH = "lu/uni/serval/ikora/sonar/I10n/ikora/rules/ikora";
    public static final String SONAR_WAY_PROFILE_NAME = "Sonar way";
    public static final String SONAR_WAY_PATH = IKORA_RESOURCE_PATH + "/sonar-way-profile.json";

    private final Configuration configuration;

    public IkoraLanguage(Configuration configuration) {
        super(KEY, IKORA_LANGUAGE_NAME);
        this.configuration = configuration;
    }

    @Override
    public String[] getFileSuffixes() {
        String[] suffixes = filterEmptyStrings(configuration.getStringArray(IkoraLanguage.FILE_SUFFIXES_KEY));

        if (suffixes.length == 0) {
            suffixes = IkoraLanguage.DEFAULT_SUFFIXES;
        }

        return suffixes;
    }

    private static String[] filterEmptyStrings(String[] stringArray) {
        List<String> nonEmptyStrings = new ArrayList<>();

        for (String string : stringArray) {
            if (!string.trim().isEmpty()) {
                nonEmptyStrings.add(string.trim());
            }
        }

        return nonEmptyStrings.toArray(new String[0]);
    }
}
