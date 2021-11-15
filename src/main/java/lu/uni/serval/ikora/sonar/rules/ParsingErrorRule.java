package lu.uni.serval.ikora.sonar.rules;

import lu.uni.serval.ikora.core.error.LocalError;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.Set;

public class ParsingErrorRule extends IkoraLintRule {
    private static final Logger LOG = Loggers.get(ParsingErrorRule.class);

    protected void addViolations(Set<LocalError> errors){
        if(errors.isEmpty()){
            return;
        }

        for(LocalError error: errors){
            LOG.debug(String.format("Add error: %s", error.getMessage()));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    error.getMessage(),
                    error.getRange().getStart().getLine(),
                    error.getRange().getStart().getLineOffset());

            ikoraSourceCode.addViolation(issue);
        }
    }
}
