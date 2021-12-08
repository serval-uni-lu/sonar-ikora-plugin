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
import lu.uni.serval.ikora.core.model.UserKeyword;
import lu.uni.serval.ikora.sonar.IkoraLanguage;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

@Rule(key = MaximumNumberArgumentsRule.RULE_KEY)
public class MaximumNumberArgumentsRule extends IkoraLintRule {
    public static final String RULE_KEY = "maximum-number-arguments-rule";

    private static final Logger LOG = Loggers.get(MaximumNumberArgumentsRule.class);

    @Override
    public void validate() {
        super.validate();

        SourceFile sourceFile = ikoraSourceCode.getSourceFile();

        for(UserKeyword userKeyword: sourceFile.getUserKeywords()){
            checkNumberOfArguments(userKeyword);
        }
    }

    private void checkNumberOfArguments(UserKeyword userKeyword) {
        int maxArgs = getInt(IkoraLanguage.MAXIMUM_NUMBER_ARGS, 4);

        if(userKeyword.getArguments().size() > maxArgs){
            LOG.debug(String.format("Add too many arguments issue for '%s'", userKeyword));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    String.format("User keyword should have a maximum of %s arguments", maxArgs),
                    userKeyword.getDefinitionToken().getLine(),
                    userKeyword.getDefinitionToken().getStartOffset(),
                    userKeyword.getDefinitionToken().getEndOffset()
            );

            ikoraSourceCode.addViolation(issue);
        }
    }
}
