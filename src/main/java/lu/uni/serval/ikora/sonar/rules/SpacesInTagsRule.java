package lu.uni.serval.ikora.sonar.rules;

import lu.uni.serval.ikora.core.model.*;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

@Rule(key = SpacesInTagsRule.RULE_KEY)
public class SpacesInTagsRule extends IkoraLintRule {
    public static final String RULE_KEY = "spaces-in-tags-rule";

    private static final Logger LOG = Loggers.get(SpacesInTagsRule.class);

    @Override
    public void validate() {
        super.validate();

        SourceFile sourceFile = ikoraSourceCode.getSourceFile();

        for(TestCase testCase: sourceFile.getTestCases()){
            checkSpaces(testCase);
        }

        for(UserKeyword userKeyword: sourceFile.getUserKeywords()){
            checkSpaces(userKeyword);
        }
    }

    private void checkSpaces(KeywordDefinition keyword) {
        for(Literal tag: keyword.getTags()){
            if(tag.toString().contains(" ")){
                LOG.debug(String.format("Add issue 'found space' found in '%s'", keyword));

                IkoraIssue issue = new IkoraIssue(ruleKey,
                        "Spaces should be avoided in tags because they may lead to confusion to their delimitation.",
                        tag.getNameToken().getLine(),
                        tag.getNameToken().getStartOffset(),
                        tag.getNameToken().getEndOffset()
                );

                ikoraSourceCode.addViolation(issue);
            }
        }
    }
}
