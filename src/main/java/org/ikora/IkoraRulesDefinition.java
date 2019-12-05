package org.ikora;

import org.sonar.api.server.rule.RulesDefinition;

public class IkoraRulesDefinition implements RulesDefinition {
    @Override
    public void define(Context context) {
        NewRepository repository = context
                .createRepository(IkoraLanguage.REPOSITORY_KEY, IkoraLanguage.KEY)
                .setName(IkoraLanguage.REPOSITORY_NAME);
    }
}
