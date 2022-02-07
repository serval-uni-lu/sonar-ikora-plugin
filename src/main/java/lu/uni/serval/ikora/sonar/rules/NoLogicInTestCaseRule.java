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
            boolean isBranching = keyword.isPresent() && keyword.get().getType() == Keyword.Type.BRANCHING;

            if(isBranching || (step instanceof ForLoop && !testCase.hasTemplate())){
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
