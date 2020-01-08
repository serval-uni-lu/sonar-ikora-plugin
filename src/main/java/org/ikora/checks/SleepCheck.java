package org.ikora.checks;

import org.ikora.libraries.builtin.Sleep;
import org.ikora.model.*;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

@Rule(key = SleepCheck.RULE_KEY)
public class SleepCheck extends IkoraLintCheck {
    public static final String RULE_KEY = "SleepCheck";

    private static final Logger LOG = Loggers.get(SleepCheck.class);

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
                if(k instanceof Sleep){
                    LOG.debug(String.format("Add issue 'sleep' found in '%s'", keyword.getName()));

                    IkoraIssue issue = new IkoraIssue(ruleKey,
                            "Sleep keyword should be avoided in production",
                            step.getPosition().getStartMark().getLine(),
                            step.getPosition().getStartMark().getColumn());

                    ikoraSourceCode.addViolation(issue);
                }
            });
        }
    }
}
