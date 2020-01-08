package org.ikora.checks;

import org.ikora.model.KeywordDefinition;
import org.ikora.model.SourceFile;
import org.ikora.model.TestCase;
import org.ikora.model.UserKeyword;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

@Rule(key = KeywordHasMinimumOneStepCheck.RULE_KEY)
public class KeywordHasMinimumOneStepCheck extends IkoraLintCheck {
    public static final String RULE_KEY = "KeywordHasMinimumOneStepCheck";

    private static final Logger LOG = Loggers.get(KeywordHasMinimumOneStepCheck.class);

    @Override
    public void validate() {
        super.validate();

        SourceFile sourceFile = ikoraSourceCode.getSourceFile();

        for(TestCase testCase: sourceFile.getTestCases()){
            checkNumberStep(testCase);
        }

        for(UserKeyword userKeyword: sourceFile.getUserKeywords()){
            checkNumberStep(userKeyword);
        }
    }

    private void checkNumberStep(KeywordDefinition keyword){
        if(keyword.getSteps().isEmpty()){
            LOG.debug(String.format("Add missing steps issue for '%s'", keyword.getName()));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    "Keyword definition needs to have at least one step",
                    keyword.getPosition().getStartMark().getLine(),
                    keyword.getPosition().getStartMark().getColumn());

            ikoraSourceCode.addViolation(issue);
        }
    }
}
