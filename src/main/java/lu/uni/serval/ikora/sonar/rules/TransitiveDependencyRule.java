package lu.uni.serval.ikora.sonar.rules;

import lu.uni.serval.ikora.core.analytics.violations.Violation;
import lu.uni.serval.ikora.core.analytics.violations.ViolationDetection;
import lu.uni.serval.ikora.core.model.SourceFile;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

import java.util.List;

@Rule(key = TransitiveDependencyRule.RULE_KEY)
public class TransitiveDependencyRule extends IkoraLintRule {
    public static final String RULE_KEY = "transitive-dependency-rule";

    private static final Logger LOG = Loggers.get(TransitiveDependencyRule.class);

    @Override
    public void validate() {
        super.validate();

        SourceFile sourceFile = ikoraSourceCode.getSourceFile();
        List<Violation> violations = ViolationDetection.detect(sourceFile, Violation.Cause.TRANSITIVE_DEPENDENCY);

        for(Violation violation: violations){
            LOG.debug(String.format("Add transitive dependency issue for '%s'", violation.getSourceNode().toString()));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    "Definition should not rely on indirect dependency",
                    violation.getSourceNode().getNameToken().getLine(),
                    violation.getSourceNode().getNameToken().getStartOffset()
            );

            ikoraSourceCode.addViolation(issue);
        }
    }
}
