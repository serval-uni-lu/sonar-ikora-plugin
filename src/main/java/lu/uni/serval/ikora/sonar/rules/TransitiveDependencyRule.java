package lu.uni.serval.ikora.sonar.rules;

import lu.uni.serval.ikora.core.analytics.violations.Violation;
import lu.uni.serval.ikora.core.analytics.violations.ViolationDetection;
import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.checks.StinkySynchronizationSyndromeCheck;
import lu.uni.serval.ikora.smells.checks.TransitiveImportCheck;
import lu.uni.serval.ikora.smells.visitors.TransitiveImportVisitor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

import java.util.List;
import java.util.Set;

@Rule(key = TransitiveDependencyRule.RULE_KEY)
public class TransitiveDependencyRule extends IkoraLintRule {
    public static final String RULE_KEY = "transitive-dependency-rule";

    private static final Logger LOG = Loggers.get(TransitiveDependencyRule.class);

    @Override
    public void validate() {
        super.validate();

        final SourceFile sourceFile = ikoraSourceCode.getSourceFile();
        final TransitiveImportCheck smellCheck = new TransitiveImportCheck();
        final Set<SourceNode> nodes = smellCheck.collectInstances(sourceFile, new SmellConfiguration());

        for(SourceNode node: nodes){
            LOG.debug(String.format("Add transitive dependency issue for '%s'", node.getAstParent().getName()));

            final IkoraIssue issue = new IkoraIssue(ruleKey,
                    "Definition should not rely on indirect dependency",
                    node.getNameToken().getLine(),
                    node.getNameToken().getStartOffset(),
                    node.getNameToken().getEndOffset()
            );

            ikoraSourceCode.addViolation(issue);
        }
    }
}
