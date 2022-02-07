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

import java.util.Arrays;
import java.util.List;

public class RulesRepository {

    private RulesRepository(){}

    private static final List<Class<? extends IkoraRule>> RULE_CLASSES = Arrays.asList(
            SyntaxErrorRule.class,
            SymbolErrorRule.class,

            ComplicatedSetupRule.class,
            ConditionalAssertionRule.class,
            DeadCodeRule.class,
            HardcodedEnvironmentConfigurationRule.class,
            HardcodedValueRule.class,
            HidingTestDataRule.class,
            LackOfEncapsulationRule.class,
            LongTestStepRule.class,
            MaximumNumberArgumentsRule.class,
            MaximumNumberTestCaseStepsRule.class,
            MiddleManRule.class,
            MinimumNumberTestCaseStepsRule.class,
            MissingAssertionRule.class,
            MissingDocumentationRule.class,
            NoisyLoggingRule.class,
            NoLogicInTestCaseRule.class,
            NoOperationRule.class,
            OnTheFlyRule.class,
            OverCheckingRule.class,
            SameDocumentationRule.class,
            SensitiveLocatorRule.class,
            SneakyCheckingRule.class,
            SpacesInTagsRule.class,
            StinkySynchronizationSyndromeRule.class,
            TestCaseWithPeriodRule.class,
            TransitiveDependencyRule.class
    );

    public static List<Class<? extends IkoraRule>> getRuleClasses(){
        return RULE_CLASSES;
    }
}
