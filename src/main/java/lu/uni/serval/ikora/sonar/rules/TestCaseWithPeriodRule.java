package lu.uni.serval.ikora.sonar.rules;

import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.TestCase;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

@Rule(key = TestCaseWithPeriodRule.RULE_KEY)
public class TestCaseWithPeriodRule extends IkoraLintRule {
    public static final String RULE_KEY = "TestCaseWithPeriodCheck";

    private static final Logger LOG = Loggers.get(TestCaseWithPeriodRule.class);

    @Override
    public void validate() {
        super.validate();

        SourceFile sourceFile = ikoraSourceCode.getSourceFile();

        for(TestCase testCase: sourceFile.getTestCases()){
            checkName(testCase);
        }
    }

    private void checkName(TestCase testCase) {
        if(testCase.getName().contains(".")){
            LOG.debug(String.format("Add period in name issue for test case '%s'", testCase));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    "Test case name should not contain periods",
                    testCase.getNameToken().getLine(),
                    testCase.getNameToken().getStartOffset());

            ikoraSourceCode.addViolation(issue);
        }
    }
}
