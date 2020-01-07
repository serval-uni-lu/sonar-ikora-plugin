package org.ikora.checks;

import org.ikora.IkoraSensor;
import org.ikora.error.LocalError;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.Set;

public class ParsingErrorCheck extends IkoraLintCheck {
    private static final Logger LOG = Loggers.get(IkoraSensor.class);

    protected void addViolations(Set<LocalError> errors){
        if(errors.isEmpty()){
            return;
        }

        for(LocalError error: errors){
            LOG.info(String.format("Add error: %s", error.getMessage()));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    error.getMessage(),
                    error.getPosition().getStartMark().getLine(),
                    error.getPosition().getStartMark().getColumn());

            ikoraSourceCode.addViolation(issue);
        }
    }
}
