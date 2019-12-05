package org.ikora.checks;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class IkoraLintCheck extends IkoraCheck {
    private static final Logger LOGGER = Loggers.get(IkoraLintCheck.class);

    @Override
    public void validate() {
        if(ikoraSourceCode == null){
            throw new IllegalStateException("Source code not set");
        }
    }
}
