package lu.uni.serval.ikora.sonar.rules;

import lu.uni.serval.ikora.core.error.LocalError;
import org.sonar.check.Rule;

import java.util.HashSet;
import java.util.Set;

@Rule(key = SymbolErrorRule.RULE_KEY)
public class SymbolErrorRule extends ParsingErrorRule {
    public static final String RULE_KEY = "SymbolErrorCheck";

    @Override
    public void validate() {
        super.validate();
        Set<LocalError> errors = new HashSet<>(ikoraSourceCode.getErrors().getSymbolErrors());
        addViolations(errors);
    }
}
