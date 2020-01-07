package org.ikora.checks;

import org.ikora.model.SourceFile;
import org.ikora.model.UserKeyword;
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
                LOG.info(String.format("Add missing documentation for: %s", keyword.getName()));

                IkoraIssue issue = new IkoraIssue(ruleKey,
                        "Missing documentation",
                        keyword.getPosition().getStartMark().getLine(),
                        keyword.getPosition().getStartMark().getColumn());

                ikoraSourceCode.addViolation(issue);
            }
        }
    }
}
