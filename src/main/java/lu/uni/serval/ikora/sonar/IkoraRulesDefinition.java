package lu.uni.serval.ikora.sonar;

import lu.uni.serval.ikora.sonar.rules.RulesRepository;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonarsource.analyzer.commons.RuleMetadataLoader;

import java.util.ArrayList;

public class IkoraRulesDefinition implements RulesDefinition {
    @Override
    public void define(Context context) {
        NewRepository repository = context
                .createRepository(IkoraLanguage.REPOSITORY_KEY, IkoraLanguage.KEY)
                .setName(IkoraLanguage.REPOSITORY_NAME);

        RuleMetadataLoader ruleMetadataLoader = new RuleMetadataLoader(IkoraLanguage.IKORA_RESOURCE_PATH,
                                                                        IkoraLanguage.SONAR_WAY_PATH);

        // add the new checks
        ruleMetadataLoader.addRulesByAnnotatedClass(repository, new ArrayList<>(RulesRepository.getRuleClasses()));

        repository.done();
    }
}
