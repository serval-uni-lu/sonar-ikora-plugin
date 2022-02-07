package lu.uni.serval.ikora.sonar.rules;

/*-
 * #%L
 * sonar-ikora-plugin
 * %%
 * Copyright (C) 2020 - 2021 University of Luxembourg
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

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
