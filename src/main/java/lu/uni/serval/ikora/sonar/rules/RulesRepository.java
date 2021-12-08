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
