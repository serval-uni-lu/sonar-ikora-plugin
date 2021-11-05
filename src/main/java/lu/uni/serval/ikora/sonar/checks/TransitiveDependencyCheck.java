package lu.uni.serval.ikora.sonar.checks;

import lu.uni.serval.ikora.core.analytics.violations.Violation;
import lu.uni.serval.ikora.core.analytics.violations.ViolationDetection;
import lu.uni.serval.ikora.core.model.SourceFile;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

import java.util.List;

@Rule(key = TransitiveDependencyCheck.RULE_KEY)
public class TransitiveDependencyCheck extends IkoraLintCheck {
    public static final String RULE_KEY = "TransitiveDependencyCheck";

    private static final Logger LOG = Loggers.get(TransitiveDependencyCheck.class);

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
