package lu.uni.serval.ikora.sonar.rules;

import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.checks.SneakyCheckingCheck;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

import java.util.Set;

@Rule(key = SneakyCheckingRule.RULE_KEY)
public class SneakyCheckingRule extends IkoraLintRule {
    public static final String RULE_KEY = "sneaky-checking-rule";
    private static final Logger LOG = Loggers.get(SneakyCheckingRule.class);

    @Override
    public void validate() {
        super.validate();

        final SourceFile sourceFile = ikoraSourceCode.getSourceFile();
        final Set<SourceNode> nodes = new SneakyCheckingCheck().collectInstances(sourceFile, new SmellConfiguration());

        for(SourceNode node: nodes){
            LOG.debug(String.format("Add issue '%s' found in '%s'", RULE_KEY, node.getSourceFile().getName()));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    "The assertion is hidden from the reader by adding levels of indirection.",
                    node.getNameToken().getLine(),
                    node.getNameToken().getStartOffset(),
                    node.getNameToken().getEndOffset()
            );

            ikoraSourceCode.addViolation(issue);
        }
    }
}
