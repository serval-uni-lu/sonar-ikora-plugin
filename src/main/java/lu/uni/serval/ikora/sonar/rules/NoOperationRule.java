package lu.uni.serval.ikora.sonar.rules;

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

import lu.uni.serval.ikora.core.libraries.builtin.keywords.NoOperation;
import lu.uni.serval.ikora.core.model.*;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

@Rule(key = NoOperationRule.RULE_KEY)
public class NoOperationRule extends IkoraLintRule {
    public static final String RULE_KEY = "no-operation-rule";

    private static final Logger LOG = Loggers.get(NoOperationRule.class);

    @Override
    public void validate() {
        super.validate();

        SourceFile sourceFile = ikoraSourceCode.getSourceFile();

        for(TestCase testCase: sourceFile.getTestCases()){
            checkNoOperation(testCase);
        }

        for(UserKeyword userKeyword: sourceFile.getUserKeywords()){
            checkNoOperation(userKeyword);
        }
    }

    private void checkNoOperation(KeywordDefinition keyword) {
        for(Step step: keyword.getSteps()){
            step.getKeywordCall().flatMap(KeywordCall::getKeyword).ifPresent(k -> {
                if(k instanceof NoOperation){
                    LOG.debug(String.format("Add issue no operation found in '%s'", keyword));

                    IkoraIssue issue = new IkoraIssue(ruleKey,
                            "No operation keyword should be avoided in production",
                            step.getDefinitionToken().getLine(),
                            step.getDefinitionToken().getStartOffset(),
                            step.getDefinitionToken().getEndOffset()
                    );

                    ikoraSourceCode.addViolation(issue);
                }
            });
        }
    }
}
