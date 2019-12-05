package org.ikora.checks;

import java.util.Arrays;
import java.util.List;

public class CheckRepository {

    private CheckRepository(){}

    private static final List<Class<? extends IkoraCheck>> CHECK_CLASSES = Arrays.asList(
            ParsingErrorCheck.class
    );

    public static List<Class<? extends IkoraCheck>> getCheckClasses(){
        return CHECK_CLASSES;
    }

    public static Class<? extends IkoraCheck> getParsingErrorCheckClass(){
        return ParsingErrorCheck.class;
    }
}
