package tech.ikora.checks;

import tech.ikora.model.*;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

import java.util.Optional;

@Rule(key = NoLogicInTestCaseCheck.RULE_KEY)
public class NoLogicInTestCaseCheck extends IkoraLintCheck {
    public static final String RULE_KEY = "NoLogicInTestCaseCheck";

    private static final Logger LOG = Loggers.get(NoLogicInTestCaseCheck.class);

    @Override
    public void validate() {
        super.validate();

        SourceFile sourceFile = ikoraSourceCode.getSourceFile();

        for(TestCase testCase: sourceFile.getTestCases()){
            checkLogicSteps(testCase);
        }
    }

    private void checkLogicSteps(TestCase testCase) {
        for(Step step: testCase.getSteps()){
            Optional<Keyword> keyword = step.getKeywordCall().flatMap(KeywordCall::getKeyword);
            boolean isControlFlow = keyword.isPresent() && keyword.get().getType() == Keyword.Type.CONTROL_FLOW;

            if(isControlFlow || (step instanceof ForLoop && !testCase.hasTemplate())){
                LOG.debug(String.format("Add issue no logic steps allowed in '%s'", testCase.toString()));

                IkoraIssue issue = new IkoraIssue(ruleKey,
                        "No control flow operation should be present in a test case",
                        step.getPosition().getStartMark().getLine(),
                        step.getPosition().getStartMark().getColumn());

                ikoraSourceCode.addViolation(issue);
            }
        }
    }
}
