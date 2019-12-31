package org.ikora;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonarsource.analyzer.commons.BuiltInQualityProfileJsonLoader;

public class IkoraQualityProfile implements BuiltInQualityProfilesDefinition {

    @Override
    public void define(Context context) {
        NewBuiltInQualityProfile sonarWay = context.createBuiltInQualityProfile(
                IkoraLanguage.SONAR_WAY_PROFILE_NAME,
                IkoraLanguage.KEY
        );

        BuiltInQualityProfileJsonLoader.load(sonarWay, IkoraLanguage.REPOSITORY_KEY, IkoraLanguage.SONAR_WAY_PATH);

        sonarWay.done();
    }
}
