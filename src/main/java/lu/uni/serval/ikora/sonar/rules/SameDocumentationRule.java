package lu.uni.serval.ikora.sonar.rules;

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.checks.SameDocumentationCheck;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

import java.util.Set;

@Rule(key = SameDocumentationRule.RULE_KEY)
public class SameDocumentationRule extends IkoraLintRule {
    public static final String RULE_KEY = "DocumentationDifferentThanNameCheck";
    private static final Logger LOG = Loggers.get(SameDocumentationRule.class);

    @Override
    public void validate() {
        super.validate();

        final SourceFile sourceFile = ikoraSourceCode.getSourceFile();
        final SameDocumentationCheck sameDocumentationCheck = new SameDocumentationCheck();
        final Set<SourceNode> nodes = sameDocumentationCheck.collectInstances(sourceFile, new SmellConfiguration());

        for(SourceNode keyword: nodes){
            LOG.debug(String.format("Add documentation same as name issue for '%s'", keyword));

            final IkoraIssue issue = new IkoraIssue(ruleKey,
                    "Documentation should not be the same as the name",
                    keyword.getNameToken().getLine(),
                    keyword.getNameToken().getStartOffset());

            ikoraSourceCode.addViolation(issue);
        }
    }
}
