package org.ikora;

import org.sonar.api.SonarRuntime;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

public class IkoraQualityProfile implements BuiltInQualityProfilesDefinition {
    private final SonarRuntime sonarRuntime;

    public IkoraQualityProfile(SonarRuntime sonarRuntime){
        this.sonarRuntime = sonarRuntime;
    }

    @Override
    public void define(Context context) {
        NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(
                IkoraLanguage.SONAR_WAY_PROFILE_NAME,
                IkoraLanguage.KEY
        );

        //profile.activateRule(IkoraLanguage.REPOSITORY_KEY, "ParsingErrorCheck");

        profile.done();
    }
}
