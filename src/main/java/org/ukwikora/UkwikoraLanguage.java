package org.ukwikora;

import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;

import java.util.ArrayList;
import java.util.List;

public class UkwikoraLanguage extends AbstractLanguage {

    public static final String KEY = "ukwikora";
    public static final String REPOSITORY_KEY = "ukwikora";
    public static final String REPOSITORY_NAME = "SonarAnalyzer";

    private static final String[] DEFAULT_SUFFIXES = {".robot"};
    private static final String UKWIKORA_LANGUAGE_NAME = "Robot Framework";

    public static final String UKWIKORA_RESOURCE_PATH = "org/ukwikora/l10n/ukwikora/rules/ukwikora";
    public static final String SONAR_WAY_PROFILE_NAME = "Sonar way";
    public static final String SONAR_WAY_PATH = "org/sonar/l10n/ukwikora/rules/ukwikora/Sonar_way_profile.json";

    private final Configuration configuration;

    public UkwikoraLanguage(Configuration configuration) {
        super(KEY, UKWIKORA_LANGUAGE_NAME);
        this.configuration = configuration;
    }


    @Override
    public String[] getFileSuffixes() {
        String[] suffixes = filterEmptyStrings(configuration.getStringArray(UkwikoraPlugin.FILE_SUFFIXES_KEY));

        if (suffixes.length == 0) {
            suffixes = UkwikoraLanguage.DEFAULT_SUFFIXES;
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
