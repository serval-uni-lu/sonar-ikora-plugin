package lu.uni.serval.ikora.sonar.rules;

import lu.uni.serval.ikora.core.error.LocalError;
import org.sonar.check.Rule;

import java.util.HashSet;
import java.util.Set;

@Rule(key = SyntaxErrorRule.RULE_KEY)
public class SyntaxErrorRule extends ParsingErrorRule {
    public static final String RULE_KEY = "syntax-error-rule";

    @Override
    public void validate() {
        super.validate();
        Set<LocalError> errors = new HashSet<>(ikoraSourceCode.getErrors().getSyntaxErrors());
        addViolations(errors);
    }
}
