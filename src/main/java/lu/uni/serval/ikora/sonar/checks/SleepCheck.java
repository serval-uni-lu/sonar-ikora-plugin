package lu.uni.serval.ikora.sonar.checks;

import lu.uni.serval.ikora.core.libraries.builtin.keywords.Sleep;
import lu.uni.serval.ikora.core.model.*;
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
            checkSleep(testCase);
        }

        for(UserKeyword userKeyword: sourceFile.getUserKeywords()){
            checkSleep(userKeyword);
        }
    }

    private void checkSleep(KeywordDefinition keyword) {
        for(Step step: keyword.getSteps()){
            step.getKeywordCall().flatMap(KeywordCall::getKeyword).ifPresent(k -> {
                if(k instanceof Sleep){
                    LOG.debug(String.format("Add issue 'sleep' found in '%s'", keyword));

                    IkoraIssue issue = new IkoraIssue(ruleKey,
                            "Sleep keyword should be avoided in production",
                            step.getNameToken().getLine(),
                            step.getNameToken().getStartOffset());

                    ikoraSourceCode.addViolation(issue);
                }
            });
        }
    }
}
