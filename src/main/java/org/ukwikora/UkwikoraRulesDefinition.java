package org.ukwikora;

import org.sonar.api.server.rule.RulesDefinition;

public class UkwikoraRulesDefinition implements RulesDefinition {
    @Override
    public void define(Context context) {
        NewRepository repository = context
                .createRepository(UkwikoraLanguage.REPOSITORY_KEY, UkwikoraLanguage.KEY)
                .setName(UkwikoraLanguage.REPOSITORY_NAME);
    }
}
