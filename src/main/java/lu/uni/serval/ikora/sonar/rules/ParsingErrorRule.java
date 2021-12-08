package lu.uni.serval.ikora.sonar.rules;

/*-
 * #%L
 * sonar-ikora-plugin
 * %%
 * Copyright (C) 2020 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
