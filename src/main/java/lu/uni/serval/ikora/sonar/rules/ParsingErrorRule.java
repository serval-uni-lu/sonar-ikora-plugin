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
                    error.getRange().getStart().getLineOffset(),
                    error.getRange().getEnd().getLineOffset()
            );

            ikoraSourceCode.addViolation(issue);
        }
    }
}
