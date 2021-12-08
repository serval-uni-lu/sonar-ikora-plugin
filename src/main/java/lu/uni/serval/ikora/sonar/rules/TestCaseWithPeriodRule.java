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
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

@Rule(key = TestCaseWithPeriodRule.RULE_KEY)
public class TestCaseWithPeriodRule extends IkoraLintRule {
    public static final String RULE_KEY = "testcase-with-period-rule";

    private static final Logger LOG = Loggers.get(TestCaseWithPeriodRule.class);

    @Override
    public void validate() {
        super.validate();

        SourceFile sourceFile = ikoraSourceCode.getSourceFile();

        for(TestCase testCase: sourceFile.getTestCases()){
            checkName(testCase);
        }
    }

    private void checkName(TestCase testCase) {
        if(testCase.getName().contains(".")){
            LOG.debug(String.format("Add period in name issue for test case '%s'", testCase));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    "Test case name should not contain periods to avoid confusion with suites.",
                    testCase.getDefinitionToken().getLine(),
                    testCase.getDefinitionToken().getStartOffset(),
                    testCase.getDefinitionToken().getEndOffset()
            );

            ikoraSourceCode.addViolation(issue);
        }
    }
}
