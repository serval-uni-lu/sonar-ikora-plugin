package org.ikora.checks;

import org.sonar.check.Rule;

@Rule(key = ParsingErrorCheck.RULE_KEY)
public class ParsingErrorCheck extends IkoraLintCheck {
    public static final String RULE_KEY = "E001";

    @Override
    public void validate() {
        super.validate();
    }
}
