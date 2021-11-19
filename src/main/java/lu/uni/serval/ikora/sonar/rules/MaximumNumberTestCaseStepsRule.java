package lu.uni.serval.ikora.sonar.rules;

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
                    testCase.getNameToken().getLine(),
                    testCase.getNameToken().getStartOffset(),
                    testCase.getNameToken().getEndOffset()
            );

            ikoraSourceCode.addViolation(issue);
        }
    }
}
