package org.ikora.checks;

import org.ikora.IkoraSourceCode;
import org.sonar.api.rule.RuleKey;

public abstract class IkoraCheck {
    protected RuleKey ruleKey = null;
    protected IkoraSourceCode ikoraSourceCode = null;

    public final void setRuleKey(RuleKey ruleKey){
        this.ruleKey = ruleKey;
    }

    public RuleKey getRuleKey() {
        return ruleKey;
    }

    public IkoraSourceCode getIkoraSourceCode() {
        return ikoraSourceCode;
    }

    public void setIkoraSourceCode(IkoraSourceCode ikoraSourceCode) {
        this.ikoraSourceCode = ikoraSourceCode;
    }

    public abstract void validate();
}
