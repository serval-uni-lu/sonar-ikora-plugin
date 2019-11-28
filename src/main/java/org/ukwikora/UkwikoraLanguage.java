package org.ukwikora;

import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;

import java.util.ArrayList;
import java.util.List;

public class UkwikoraLanguage extends AbstractLanguage {

    private static final String[] DEFAULT_SUFFIXES = {".robot"};
    public static final String KEY = "ukwikora";
    private static final String UKWIKORA_LANGUAGE_NAME = "Robot Framework";

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
