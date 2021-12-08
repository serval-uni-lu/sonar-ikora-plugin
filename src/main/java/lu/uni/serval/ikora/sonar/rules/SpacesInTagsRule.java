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
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

@Rule(key = SpacesInTagsRule.RULE_KEY)
public class SpacesInTagsRule extends IkoraLintRule {
    public static final String RULE_KEY = "spaces-in-tags-rule";

    private static final Logger LOG = Loggers.get(SpacesInTagsRule.class);

    @Override
    public void validate() {
        super.validate();

        SourceFile sourceFile = ikoraSourceCode.getSourceFile();

        for(TestCase testCase: sourceFile.getTestCases()){
            checkSpaces(testCase);
        }

        for(UserKeyword userKeyword: sourceFile.getUserKeywords()){
            checkSpaces(userKeyword);
        }
    }

    private void checkSpaces(KeywordDefinition keyword) {
        for(Literal tag: keyword.getTags()){
            if(tag.toString().contains(" ")){
                LOG.debug(String.format("Add issue 'found space' found in '%s'", keyword));

                IkoraIssue issue = new IkoraIssue(ruleKey,
                        "Spaces should be avoided in tags because they may lead to confusion to their delimitation.",
                        tag.getDefinitionToken().getLine(),
                        tag.getDefinitionToken().getStartOffset(),
                        tag.getDefinitionToken().getEndOffset()
                );

                ikoraSourceCode.addViolation(issue);
            }
        }
    }
}
