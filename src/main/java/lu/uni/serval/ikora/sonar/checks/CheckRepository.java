package lu.uni.serval.ikora.sonar.checks;

import java.util.Arrays;
import java.util.List;

public class CheckRepository {

    private CheckRepository(){}

    private static final List<Class<? extends IkoraCheck>> CHECK_CLASSES = Arrays.asList(
            SyntaxErrorCheck.class,
            SymbolErrorCheck.class,
            KeywordDocumentationCheck.class,
            KeywordHasMinimumOneStepCheck.class,
            MaximumNumberOfArgumentsCheck.class,
            NoOperationCheck.class,
            TestHasLessThan10StepsCheck.class,
            DocumentationDifferentThanNameCheck.class,
            SleepCheck.class,
            TestCaseHasMinimumNumberOfSteps.class,
            NoLogicInTestCaseCheck.class,
            TransitiveDependencyCheck.class,
            TestCaseWithPeriodCheck.class,
            SpacesInTagsCheck.class
    );

    public static List<Class<? extends IkoraCheck>> getCheckClasses(){
        return CHECK_CLASSES;
    }
}
