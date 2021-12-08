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

import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.checks.ComplicatedSetupCheck;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

import java.util.Set;

@Rule(key = ComplicatedSetupRule.RULE_KEY)
public class ComplicatedSetupRule extends IkoraLintRule {
    public static final String RULE_KEY = "complicated-setup-rule";
    private static final Logger LOG = Loggers.get(ComplicatedSetupRule.class);

    @Override
    public void validate() {
        super.validate();

        final SourceFile sourceFile = ikoraSourceCode.getSourceFile();
        final Set<SourceNode> nodes = new ComplicatedSetupCheck().collectInstances(sourceFile, new SmellConfiguration());

        for(SourceNode node: nodes){
            LOG.debug(String.format("Add issue '%s' found in '%s'", RULE_KEY, node.getSourceFile().getName()));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    "Complicated setup may create hard to understand tests as a lot of thing are happening outside the proper steps",
                    node.getDefinitionToken().getLine(),
                    node.getDefinitionToken().getStartOffset(),
                    node.getDefinitionToken().getEndOffset()
            );

            ikoraSourceCode.addViolation(issue);
        }
    }
}
