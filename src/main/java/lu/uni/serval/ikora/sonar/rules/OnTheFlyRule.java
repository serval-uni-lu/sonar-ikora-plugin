package lu.uni.serval.ikora.sonar.rules;

import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.checks.OnTheFlyCheck;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

import java.util.Set;

@Rule(key = OnTheFlyRule.RULE_KEY)
public class OnTheFlyRule extends IkoraLintRule {
    public static final String RULE_KEY = "on-the-fly-rule";
    private static final Logger LOG = Loggers.get(OnTheFlyRule.class);

    @Override
    public void validate() {
        super.validate();

        final SourceFile sourceFile = ikoraSourceCode.getSourceFile();
        final Set<SourceNode> nodes = new OnTheFlyCheck().collectInstances(sourceFile, new SmellConfiguration());

        for(SourceNode node: nodes){
            LOG.debug(String.format("Add issue '%s' found in '%s'", RULE_KEY, node.getSourceFile().getName()));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    "Too many logs can make the test output harder to understand.",
                    node.getNameToken().getLine(),
                    node.getNameToken().getStartOffset());

            ikoraSourceCode.addViolation(issue);
        }
    }
}
