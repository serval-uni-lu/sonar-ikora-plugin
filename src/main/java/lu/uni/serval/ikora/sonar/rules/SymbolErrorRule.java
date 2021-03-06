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
import org.sonar.check.Rule;

import java.util.HashSet;
import java.util.Set;

@Rule(key = SymbolErrorRule.RULE_KEY)
public class SymbolErrorRule extends ParsingErrorRule {
    public static final String RULE_KEY = "symbol-error-rule";

    @Override
    public void validate() {
        super.validate();
        Set<LocalError> errors = new HashSet<>(ikoraSourceCode.getErrors().getSymbolErrors());
        addViolations(errors);
    }
}
