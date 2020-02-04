package org.ikora;

import org.junit.jupiter.api.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarRuntime;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;

import static org.assertj.core.api.Assertions.assertThat;

public class IkoraPluginTest {
    private static final Version VERSION_7_9 = Version.create(7, 9);
    private IkoraPlugin plugin = new IkoraPlugin();

    @Test
    public void sonarLint_7_9_extensions() {
        SonarRuntime runtime = SonarRuntimeImpl.forSonarLint(VERSION_7_9);
        Plugin.Context context = new Plugin.Context(runtime);
        plugin.define(context);
        assertThat(context.getExtensions()).hasSize(9);
    }

    @Test
    public void sonarQube_7_9_extensions() {
        Plugin.Context context = new Plugin.Context(Helpers.SQ_79_RUNTIME);
        plugin.define(context);
        assertThat(context.getExtensions()).hasSize(9);
    }
}
