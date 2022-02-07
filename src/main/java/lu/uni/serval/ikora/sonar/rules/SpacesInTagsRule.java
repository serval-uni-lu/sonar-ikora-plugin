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
