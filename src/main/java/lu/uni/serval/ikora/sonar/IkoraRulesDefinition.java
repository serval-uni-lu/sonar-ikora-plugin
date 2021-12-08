package lu.uni.serval.ikora.sonar;

/*-
 * #%L
 * sonar-ikora-plugin
 * %%
 * Copyright (C) 2020 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
