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

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.checks.SameDocumentationCheck;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

import java.util.Set;

@Rule(key = SameDocumentationRule.RULE_KEY)
public class SameDocumentationRule extends IkoraLintRule {
    public static final String RULE_KEY = "same-documentation-rule";
    private static final Logger LOG = Loggers.get(SameDocumentationRule.class);

    @Override
    public void validate() {
        super.validate();

        final SourceFile sourceFile = ikoraSourceCode.getSourceFile();
        final SameDocumentationCheck sameDocumentationCheck = new SameDocumentationCheck();
        final Set<SourceNode> nodes = sameDocumentationCheck.collectInstances(sourceFile, new SmellConfiguration());

        for(SourceNode keyword: nodes){
            LOG.debug(String.format("Add documentation same as name issue for '%s'", keyword));

            final IkoraIssue issue = new IkoraIssue(ruleKey,
                    "Documentation should not be the same as the name of the object they define.",
                    keyword.getDefinitionToken().getLine(),
                    keyword.getDefinitionToken().getStartOffset(),
                    keyword.getDefinitionToken().getEndOffset()
            );

            ikoraSourceCode.addViolation(issue);
        }
    }
}
