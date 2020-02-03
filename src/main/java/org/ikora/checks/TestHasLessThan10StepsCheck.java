package org.ikora.checks;

import org.ikora.model.SourceFile;
import org.ikora.model.TestCase;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

@Rule(key = TestHasLessThan10StepsCheck.RULE_KEY)
public class TestHasLessThan10StepsCheck extends IkoraLintCheck {
    public static final String RULE_KEY = "TestHasLessThan10StepsCheck";

    private static final Logger LOG = Loggers.get(TestHasLessThan10StepsCheck.class);

    @Override
    public void validate() {
        super.validate();

        SourceFile sourceFile = ikoraSourceCode.getSourceFile();

        for(TestCase testCase: sourceFile.getTestCases()){
            checkNumberStep(testCase);
        }
    }

    private void checkNumberStep(TestCase testCase) {
        if(testCase.getSteps().size() > 10){
            LOG.debug(String.format("Add too many steps issue for test case '%s'", testCase.toString()));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    "Test case should have less than 10 steps",
                    testCase.getPosition().getStartMark().getLine(),
                    testCase.getPosition().getStartMark().getColumn());

            ikoraSourceCode.addViolation(issue);
        }
    }
}
