package lu.uni.serval.ikora.sonar.checks;

import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.UserKeyword;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

@Rule(key = KeywordDocumentationCheck.RULE_KEY)
public class KeywordDocumentationCheck extends IkoraLintCheck {
    public static final String RULE_KEY = "KeywordDocumentationCheck";

    private static final Logger LOG = Loggers.get(KeywordDocumentationCheck.class);

    @Override
    public void validate() {
        SourceFile sourceFile = ikoraSourceCode.getSourceFile();

        for(UserKeyword keyword: sourceFile.getUserKeywords()){
            if(keyword.getDocumentation().isEmpty()){
                LOG.debug(String.format("Add missing documentation issue for '%s'", keyword.toString()));

                IkoraIssue issue = new IkoraIssue(ruleKey,
                        "Missing documentation",
                        keyword.getNameToken().getLine(),
                        keyword.getNameToken().getLine()
                );

                ikoraSourceCode.addViolation(issue);
            }
        }
    }
}
