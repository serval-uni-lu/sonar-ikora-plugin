package org.ikora.checks;

import org.ikora.model.KeywordDefinition;
import org.ikora.model.SourceFile;
import org.ikora.model.TestCase;
import org.ikora.model.UserKeyword;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

@Rule(key = DocumentationDifferentThanNameCheck.RULE_KEY)
public class DocumentationDifferentThanNameCheck extends IkoraLintCheck{
    public static final String RULE_KEY = "DocumentationDifferentThanNameCheck";

    private static final Logger LOG = Loggers.get(DocumentationDifferentThanNameCheck.class);

    @Override
    public void validate() {
        super.validate();

        SourceFile sourceFile = ikoraSourceCode.getSourceFile();

        for(TestCase testCase: sourceFile.getTestCases()){
            checkDocumentation(testCase);
        }

        for(UserKeyword userKeyword: sourceFile.getUserKeywords()){
            checkDocumentation(userKeyword);
        }
    }

    private void checkDocumentation(KeywordDefinition keyword) {
        String name = keyword.toString();
        String documentation = keyword.getDocumentation();

        if(name.equalsIgnoreCase(documentation)){
            LOG.debug(String.format("Add documentation same as name issue for '%s'", keyword.toString()));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    "Documentation should not be the same as the name",
                    keyword.getPosition().getStartMark().getLine(),
                    keyword.getPosition().getStartMark().getColumn());

            ikoraSourceCode.addViolation(issue);
        }
    }
}
