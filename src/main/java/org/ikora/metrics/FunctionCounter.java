package org.ikora.metrics;

import org.ikora.IkoraSourceCode;
import org.ikora.model.SourceFile;
import org.ikora.model.Step;
import org.ikora.model.UserKeyword;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.HashSet;
import java.util.Set;

public class FunctionCounter {
    private static final Logger LOGGER = Loggers.get(FunctionCounter.class);

    private FunctionCounter(){}

    public static void analyse(SensorContext context, FileLinesContextFactory fileLinesContextFactory, IkoraSourceCode sourceCode){
        LOGGER.debug(String.format("Get functions in %s", sourceCode.getInputFile().filename()));

        Set<Integer> functions = new HashSet<>();
        Set<Integer> statements = new HashSet<>();

        analyzeFunctions(sourceCode.getSourceFile(), functions, statements);

        MetricUtils.saveMeasure(context, sourceCode.getInputFile(), CoreMetrics.FUNCTIONS, functions.size());
        MetricUtils.saveMeasure(context, sourceCode.getInputFile(), CoreMetrics.STATEMENTS, statements.size());

        FileLinesContext fileLinesContext = fileLinesContextFactory.createFor(sourceCode.getInputFile());
        statements.forEach(statement -> fileLinesContext.setIntValue(CoreMetrics.EXECUTABLE_LINES_DATA_KEY, statement, 1));
        fileLinesContext.save();
    }

    private static void analyzeFunctions(SourceFile sourceFile, Set<Integer> functions, Set<Integer> statements) {
        for(UserKeyword keyword: sourceFile.getUserKeywords()){
            functions.add(keyword.getName().getLine());

            for(Step step: keyword.getSteps()){
                analyzeSteps(step, statements);
            }
        }
    }

    private static void analyzeSteps(Step step, Set<Integer> statements){
        statements.add(step.getPosition().getStartMark().getLine());

        for(Step step1: step.getSteps()){
            analyzeSteps(step1, statements);
        }
    }
}
