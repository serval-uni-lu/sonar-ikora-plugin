package lu.uni.serval.ikora.sonar.rules;

import lu.uni.serval.ikora.core.libraries.builtin.keywords.NoOperation;
import lu.uni.serval.ikora.core.model.*;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

@Rule(key = NoOperationRule.RULE_KEY)
public class NoOperationRule extends IkoraLintRule {
    public static final String RULE_KEY = "no-operation-rule";

    private static final Logger LOG = Loggers.get(NoOperationRule.class);

    @Override
    public void validate() {
        super.validate();

        SourceFile sourceFile = ikoraSourceCode.getSourceFile();

        for(TestCase testCase: sourceFile.getTestCases()){
            checkNoOperation(testCase);
        }

        for(UserKeyword userKeyword: sourceFile.getUserKeywords()){
            checkNoOperation(userKeyword);
        }
    }

    private void checkNoOperation(KeywordDefinition keyword) {
        for(Step step: keyword.getSteps()){
            step.getKeywordCall().flatMap(KeywordCall::getKeyword).ifPresent(k -> {
                if(k instanceof NoOperation){
                    LOG.debug(String.format("Add issue no operation found in '%s'", keyword));

                    IkoraIssue issue = new IkoraIssue(ruleKey,
                            "No operation keyword should be avoided in production",
                            step.getNameToken().getLine(),
                            step.getNameToken().getStartOffset(),
                            step.getNameToken().getEndOffset()
                    );

                    ikoraSourceCode.addViolation(issue);
                }
            });
        }
    }
}
