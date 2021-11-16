package lu.uni.serval.ikora.sonar.rules;

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
        int minSteps = getInt(IkoraLanguage.MAXIMUM_NUMBER_ARGS, 3);

        if(testCase.getSteps().size() < minSteps){
            LOG.debug(String.format("Add missing steps issue for '%s'", testCase));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    "Test case needs to have at least two steps",
                    testCase.getNameToken().getLine(),
                    testCase.getNameToken().getStartOffset());

            ikoraSourceCode.addViolation(issue);
        }
    }
}
