package org.ikora.checks;

import org.ikora.IkoraLanguage;
import org.ikora.model.SourceFile;
import org.ikora.model.TestCase;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

@Rule(key = TestCaseHasMinimumNumberOfSteps.RULE_KEY)
public class TestCaseHasMinimumNumberOfSteps extends IkoraLintCheck {
    public static final String RULE_KEY = "TestCaseHasMinimumNumberOfSteps";

    private static final Logger LOG = Loggers.get(TestCaseHasMinimumNumberOfSteps.class);

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
            LOG.debug(String.format("Add missing steps issue for '%s'", testCase.toString()));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    "Test case needs to have at least two steps",
                    testCase.getPosition().getStartMark().getLine(),
                    testCase.getPosition().getStartMark().getColumn());

            ikoraSourceCode.addViolation(issue);
        }
    }
}
