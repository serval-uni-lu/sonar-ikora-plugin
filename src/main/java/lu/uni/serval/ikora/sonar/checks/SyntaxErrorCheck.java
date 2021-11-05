package lu.uni.serval.ikora.sonar.checks;

import lu.uni.serval.ikora.core.error.LocalError;
import org.sonar.check.Rule;

import java.util.HashSet;
import java.util.Set;

@Rule(key = SyntaxErrorCheck.RULE_KEY)
public class SyntaxErrorCheck extends ParsingErrorCheck {
    public static final String RULE_KEY = "SyntaxErrorCheck";

    @Override
    public void validate() {
        super.validate();
        Set<LocalError> errors = new HashSet<>(ikoraSourceCode.getErrors().getSyntaxErrors());
        addViolations(errors);
    }
}
