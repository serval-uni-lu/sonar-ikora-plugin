package lu.uni.serval.ikora.sonar.checks;

import org.sonar.api.rule.RuleKey;

public class IkoraIssue {
    protected final RuleKey ruleKey;
    protected boolean syntaxError;
    protected int line;
    protected int column;
    protected String message;

    public IkoraIssue(RuleKey ruleKey, String message, int line, int column) {
        this.ruleKey = ruleKey;
        this.syntaxError = false;
        this.message = message;
        this.line = line;
        this.column = column;
    }

    public IkoraIssue(RuleKey ruleKey, String message, int line, int column, boolean syntaxError) {
        this.ruleKey = ruleKey;
        this.message = message;
        this.line = line;
        this.column = column;
        this.syntaxError = syntaxError;
    }

    public RuleKey getRuleKey() {
        return ruleKey;
    }

    public boolean isSyntaxError() {
        return syntaxError;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "[" + this.line + ":" + this.column + "] " + ruleKey.rule() + ": " + this.message;
    }
}
