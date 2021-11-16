package lu.uni.serval.ikora.sonar.rules;

import java.util.Arrays;
import java.util.List;

public class RulesRepository {

    private RulesRepository(){}

    private static final List<Class<? extends IkoraRule>> RULE_CLASSES = Arrays.asList(
            SyntaxErrorRule.class,
            SymbolErrorRule.class,

            SensitiveLocatorRule.class,
            ComplicatedSetupRule.class,
            ConditionalAssertionRule.class,
            EagerTestRule.class,
            HardcodedEnvironmentConfigurationRule.class,
            HardcodedValueRule.class,
            HidingTestDataRule.class,
            LackOfEncapsulationRule.class,
            LongTestStepRule.class,
            MaximumNumberArgumentsRule.class,
            MiddleManRule.class,
            MissingAssertionRule.class,
            MissingDocumentationRule.class,
            NoisyLoggingRule.class,
            NoOperationRule.class,
            OnTheFlyRule.class,
            MaximumNumberTestCaseStepsRule.class,
            SameDocumentationRule.class,
            SneakyCheckingRule.class,
            StinkySynchronizationSyndromeRule.class,
            MinimumNumberTestCaseStepsRule.class,
            NoLogicInTestCaseRule.class,
            TransitiveDependencyRule.class,
            TestCaseWithPeriodRule.class,
            SpacesInTagsRule.class
    );

    public static List<Class<? extends IkoraRule>> getRuleClasses(){
        return RULE_CLASSES;
    }
}
