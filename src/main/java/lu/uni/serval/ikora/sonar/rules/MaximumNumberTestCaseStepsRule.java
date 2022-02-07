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
