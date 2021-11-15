package lu.uni.serval.ikora.sonar.rules;

import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.checks.ComplexLocatorCheck;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

import java.util.Set;

@Rule(key = ComplexLocatorRule.RULE_KEY)
public class ComplexLocatorRule extends IkoraLintRule {
    public static final String RULE_KEY = "complex-locator-rule";
    private static final Logger LOG = Loggers.get(ComplexLocatorRule.class);

    @Override
    public void validate() {
        super.validate();

        final SourceFile sourceFile = ikoraSourceCode.getSourceFile();
        final ComplexLocatorCheck check = new ComplexLocatorCheck();
        final Set<SourceNode> nodes = check.collectInstances(sourceFile, new SmellConfiguration());

        for(SourceNode node: nodes){
            LOG.debug(String.format("Add issue '%s' found in '%s'", RULE_KEY, node.getSourceFile().getName()));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    "Complex locators lead to fragile tests as they are more prone to locator breakage",
                    node.getNameToken().getLine(),
                    node.getNameToken().getStartOffset());

            ikoraSourceCode.addViolation(issue);
        }
    }
}
