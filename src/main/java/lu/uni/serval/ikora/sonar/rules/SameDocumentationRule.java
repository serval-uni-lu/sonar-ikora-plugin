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
