package tech.ikora.checks;

import tech.ikora.model.SourceFile;
import tech.ikora.model.TestCase;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

@Rule(key = TestCaseWithPeriodCheck.RULE_KEY)
public class TestCaseWithPeriodCheck extends IkoraLintCheck {
    public static final String RULE_KEY = "TestCaseWithPeriodCheck";

    private static final Logger LOG = Loggers.get(TestCaseWithPeriodCheck.class);

    @Override
    public void validate() {
        super.validate();

        SourceFile sourceFile = ikoraSourceCode.getSourceFile();

        for(TestCase testCase: sourceFile.getTestCases()){
            checkName(testCase);
        }
    }

    private void checkName(TestCase testCase) {
        if(testCase.getName().toString().contains(".")){
            LOG.debug(String.format("Add period in name issue for test case '%s'", testCase.toString()));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    "Test case name should not contain periods",
                    testCase.getPosition().getStartMark().getLine(),
                    testCase.getPosition().getStartMark().getColumn());

            ikoraSourceCode.addViolation(issue);
        }
    }
}
