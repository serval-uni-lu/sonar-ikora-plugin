package org.ukwikora;

import org.sonar.api.SonarRuntime;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

public class UkwikoraQualityProfile implements BuiltInQualityProfilesDefinition {
    private final SonarRuntime sonarRuntime;

    public UkwikoraQualityProfile(SonarRuntime sonarRuntime){
        this.sonarRuntime = sonarRuntime;
    }

    @Override
    public void define(Context context) {
        NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(
                UkwikoraLanguage.SONAR_WAY_PROFILE_NAME,
                UkwikoraLanguage.KEY
        );

        //profile.activateRule(UkwikoraLanguage.REPOSITORY_KEY, "ParsingErrorCheck");

        profile.done();
    }
}
