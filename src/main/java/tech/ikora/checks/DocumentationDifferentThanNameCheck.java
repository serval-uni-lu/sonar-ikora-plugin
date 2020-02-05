package tech.ikora.checks;

import tech.ikora.model.KeywordDefinition;
import tech.ikora.model.SourceFile;
import tech.ikora.model.TestCase;
import tech.ikora.model.UserKeyword;
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
