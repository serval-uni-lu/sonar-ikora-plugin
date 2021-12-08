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

import lu.uni.serval.ikora.core.model.*;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

import java.util.Optional;

@Rule(key = NoLogicInTestCaseRule.RULE_KEY)
public class NoLogicInTestCaseRule extends IkoraLintRule {
    public static final String RULE_KEY = "no-logic-in-testcase-rule";

    private static final Logger LOG = Loggers.get(NoLogicInTestCaseRule.class);

    @Override
    public void validate() {
        super.validate();

        final SourceFile sourceFile = ikoraSourceCode.getSourceFile();

        for(TestCase testCase: sourceFile.getTestCases()){
            checkLogicSteps(testCase);
        }
    }

    private void checkLogicSteps(TestCase testCase) {
        for(Step step: testCase.getSteps()){
            Optional<Keyword> keyword = step.getKeywordCall().flatMap(KeywordCall::getKeyword);
            boolean isControlFlow = keyword.isPresent() && keyword.get().getType() == Keyword.Type.CONTROL_FLOW;

            if(isControlFlow || (step instanceof ForLoop && !testCase.hasTemplate())){
                LOG.debug(String.format("Add issue no logic steps allowed in '%s'", testCase));

                IkoraIssue issue = new IkoraIssue(ruleKey,
                        "No control flow operation should be present in a test case",
                        step.getDefinitionToken().getLine(),
                        step.getDefinitionToken().getStartOffset(),
                        step.getDefinitionToken().getEndOffset()
                );

                ikoraSourceCode.addViolation(issue);
            }
        }
    }
}
