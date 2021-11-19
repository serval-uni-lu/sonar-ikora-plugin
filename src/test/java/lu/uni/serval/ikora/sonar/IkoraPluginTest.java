package lu.uni.serval.ikora.sonar;

import org.junit.jupiter.api.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarEdition;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;

import static org.junit.jupiter.api.Assertions.*;

class IkoraPluginTest {
    @Test
    void sonarqube_9_1_extensions() {
        Plugin.Context context = new Plugin.Context(SonarRuntimeImpl.forSonarQube(Version.create(9, 1), SonarQubeSide.SERVER, SonarEdition.COMMUNITY));

        new IkoraPlugin().define(context);
        assertEquals(10, context.getExtensions().size());
    }
}