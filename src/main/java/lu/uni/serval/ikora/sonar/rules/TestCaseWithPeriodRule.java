package lu.uni.serval.ikora.sonar.rules;

/*-
 * #%L
 * sonar-ikora-plugin
 * %%
 * Copyright (C) 2020 - 2021 University of Luxembourg
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
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
