package lu.uni.serval.ikora.sonar.rules;

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.checks.StinkySynchronizationSyndromeCheck;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

import java.util.Set;

@Rule(key = StinkySynchronizationSyndromeRule.RULE_KEY)
public class StinkySynchronizationSyndromeRule extends IkoraLintRule {
    public static final String RULE_KEY = "stinky-synchronization-syndrome-rule";

    private static final Logger LOG = Loggers.get(StinkySynchronizationSyndromeRule.class);

    @Override
    public void validate() {
        super.validate();

        final SourceFile sourceFile = ikoraSourceCode.getSourceFile();
        final StinkySynchronizationSyndromeCheck smellCheck = new StinkySynchronizationSyndromeCheck();
        final Set<SourceNode> nodes = smellCheck.collectInstances(sourceFile, new SmellConfiguration());

        for(SourceNode node: nodes){
            LOG.debug(String.format("Add issue 'sleep' found in '%s'", node.getAstParent().getName()));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    "Sleep keyword should be avoided in production",
                    node.getNameToken().getLine(),
                    node.getNameToken().getStartOffset(),
                    node.getNameToken().getEndOffset()
            );

            ikoraSourceCode.addViolation(issue);
        }
    }
}
