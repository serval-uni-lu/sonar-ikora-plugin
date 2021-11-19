package lu.uni.serval.ikora.sonar.rules;

import lu.uni.serval.ikora.sonar.IkoraTextPointer;
import lu.uni.serval.ikora.sonar.IkoraTextRange;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.rule.RuleKey;

public class IkoraIssue {
    protected final RuleKey ruleKey;
    protected boolean syntaxError;
    protected TextRange textRange;
    protected String message;

    public IkoraIssue(RuleKey ruleKey, String message, int line, int startOffset, int endOffset) {
        this.ruleKey = ruleKey;
        this.syntaxError = false;
        this.message = message;

        this.textRange = new IkoraTextRange(
                new IkoraTextPointer(line, startOffset),
                new IkoraTextPointer(line, endOffset)
        );
    }

    public IkoraIssue(RuleKey ruleKey, String message, int line, int startOffset, int endOffset, boolean syntaxError) {
        this.ruleKey = ruleKey;
        this.message = message;

        this.textRange = new IkoraTextRange(
                new IkoraTextPointer(line, startOffset),
                new IkoraTextPointer(line, endOffset)
        );

        this.syntaxError = syntaxError;
    }

    public RuleKey getRuleKey() {
        return ruleKey;
    }

    public boolean isSyntaxError() {
        return syntaxError;
    }

    public TextRange getTextRange() {
        return textRange;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return  this.textRange + " " + ruleKey.rule() + ": " + this.message;
    }
}
