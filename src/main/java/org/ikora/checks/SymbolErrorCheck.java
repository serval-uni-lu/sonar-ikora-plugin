package org.ikora.checks;

import org.ikora.error.LocalError;
import org.sonar.check.Rule;

import java.util.HashSet;
import java.util.Set;

@Rule(key = SymbolErrorCheck.RULE_KEY)
public class SymbolErrorCheck extends ParsingErrorCheck {
    public static final String RULE_KEY = "SymbolErrorCheck";

    @Override
    public void validate() {
        super.validate();
        Set<LocalError> errors = new HashSet<>(ikoraSourceCode.getErrors().getSyntaxErrors());
        addViolations(errors);
    }
}
