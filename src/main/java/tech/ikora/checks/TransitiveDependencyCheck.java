package tech.ikora.checks;

import tech.ikora.analytics.Violation;
import tech.ikora.analytics.ViolationDetection;
import tech.ikora.model.SourceFile;
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
            LOG.debug(String.format("Add transitive dependency issue for '%s'", violation.getNode().toString()));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    "Definition should not rely on indirect dependency",
                    violation.getNode().getPosition().getStartMark().getLine(),
                    violation.getNode().getPosition().getStartMark().getColumn());

            ikoraSourceCode.addViolation(issue);
        }
    }
}
