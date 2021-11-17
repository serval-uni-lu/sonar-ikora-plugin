package lu.uni.serval.ikora.sonar.rules;

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
            boolean isControlFlow = keyword.isPresent() && keyword.get().getType() == Keyword.Type.CONTROL_FLOW;

            if(isControlFlow || (step instanceof ForLoop && !testCase.hasTemplate())){
                LOG.debug(String.format("Add issue no logic steps allowed in '%s'", testCase));

                IkoraIssue issue = new IkoraIssue(ruleKey,
                        "No control flow operation should be present in a test case",
                        step.getNameToken().getLine(),
                        step.getNameToken().getStartOffset());

                ikoraSourceCode.addViolation(issue);
            }
        }
    }
}
