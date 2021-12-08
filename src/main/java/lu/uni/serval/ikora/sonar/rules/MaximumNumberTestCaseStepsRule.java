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

import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.sonar.IkoraLanguage;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

@Rule(key = MaximumNumberTestCaseStepsRule.RULE_KEY)
public class MaximumNumberTestCaseStepsRule extends IkoraLintRule {
    public static final String RULE_KEY = "maximum-number-testcase-steps-rule";

    private static final Logger LOG = Loggers.get(MaximumNumberTestCaseStepsRule.class);

    @Override
    public void validate() {
        super.validate();

        SourceFile sourceFile = ikoraSourceCode.getSourceFile();

        for(TestCase testCase: sourceFile.getTestCases()){
            checkNumberStep(testCase);
        }
    }

    private void checkNumberStep(TestCase testCase) {
        int maxSteps = getInt(IkoraLanguage.MAXIMUM_NUMBER_TEST_STEPS, 10);

        if(testCase.getExecutedSteps().size() > maxSteps){
            LOG.debug(String.format("Add too many steps issue for test case '%s'", testCase));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    String.format("Test case should have less than %d steps", maxSteps),
                    testCase.getDefinitionToken().getLine(),
                    testCase.getDefinitionToken().getStartOffset(),
                    testCase.getDefinitionToken().getEndOffset()
            );

            ikoraSourceCode.addViolation(issue);
        }
    }
}
