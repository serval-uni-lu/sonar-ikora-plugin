package lu.uni.serval.ikora.sonar.rules;

import java.util.Arrays;
import java.util.List;

public class RulesRepository {

    private RulesRepository(){}

    private static final List<Class<? extends IkoraRule>> RULE_CLASSES = Arrays.asList(
            SyntaxErrorRule.class,
            SymbolErrorRule.class,

            ComplicatedSetupRule.class,
            ConditionalAssertionRule.class,
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
            NoLogicInTestCaseRule.class,
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
