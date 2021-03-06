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

@Rule(key = MinimumNumberTestCaseStepsRule.RULE_KEY)
public class MinimumNumberTestCaseStepsRule extends IkoraLintRule {
    public static final String RULE_KEY = "minimum-number-testcase-steps-rule";
    private static final Logger LOG = Loggers.get(MinimumNumberTestCaseStepsRule.class);

    @Override
    public void validate() {
        super.validate();

        SourceFile sourceFile = ikoraSourceCode.getSourceFile();

        for(TestCase testCase: sourceFile.getTestCases()){
            checkNumberStep(testCase);
        }
    }

    private void checkNumberStep(TestCase testCase){
        int minSteps = getInt(IkoraLanguage.MINIMUM_NUMBER_TEST_STEPS, 3);

        if(testCase.getSteps().size() < minSteps){
            LOG.debug(String.format("Add missing steps issue for '%s'", testCase));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    String.format("Test case needs to have at least %s steps to tell a story.", minSteps),
                    testCase.getDefinitionToken().getLine(),
                    testCase.getDefinitionToken().getStartOffset(),
                    testCase.getDefinitionToken().getEndOffset()
            );

            ikoraSourceCode.addViolation(issue);
        }
    }
}
