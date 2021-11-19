package lu.uni.serval.ikora.sonar.rules;

import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.checks.MissingDocumentationCheck;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

import java.util.Set;

@Rule(key = MissingDocumentationRule.RULE_KEY)
public class MissingDocumentationRule extends IkoraLintRule {
    public static final String RULE_KEY = "missing-documentation-rule";
    private static final Logger LOG = Loggers.get(MissingDocumentationRule.class);

    @Override
    public void validate() {
        final SourceFile sourceFile = ikoraSourceCode.getSourceFile();
        final Set<SourceNode> nodes = new MissingDocumentationCheck().collectInstances(sourceFile, new SmellConfiguration());

        for(SourceNode node: nodes){
            LOG.debug(String.format("Add issue '%s' found in '%s'", RULE_KEY, node.getSourceFile().getName()));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    "User keywords are expected to have a documentation tag with a non empty documentation block.",
                    node.getNameToken().getLine(),
                    node.getNameToken().getStartOffset(),
                    node.getNameToken().getEndOffset()
            );

            ikoraSourceCode.addViolation(issue);
        }
    }
}
