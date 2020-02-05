package tech.ikora;

import org.junit.jupiter.api.Test;
import org.sonar.api.config.internal.MapSettings;

import static org.assertj.core.api.Assertions.assertThat;

public class IkoraLanguageTest {
    @Test
    public void should_return_java_file_suffixes() {
        MapSettings settings = new MapSettings();
        IkoraLanguage language = new IkoraLanguage(settings.asConfig());
        assertThat(language.getFileSuffixes()).containsOnly(".robot");

        settings.setProperty(IkoraLanguage.FILE_SUFFIXES_KEY, "");
        assertThat(language.getFileSuffixes()).containsOnly(".robot");

        settings.setProperty(IkoraLanguage.FILE_SUFFIXES_KEY, ".bar, .foo");
        assertThat(language.getFileSuffixes()).containsOnly(".bar", ".foo");
    }
}
