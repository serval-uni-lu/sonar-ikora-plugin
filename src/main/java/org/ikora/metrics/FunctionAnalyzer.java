package org.ikora.metrics;

import org.ikora.IkoraSourceCode;
import org.ikora.model.KeywordDefinition;
import org.ikora.model.SourceFile;
import org.ikora.model.Step;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FunctionAnalyzer {
    private static final Logger LOGGER = Loggers.get(FunctionAnalyzer.class);

    private FunctionAnalyzer(){}

    public static void analyse(SensorContext context, IkoraSourceCode sourceCode){
        LOGGER.debug(String.format("Get functions in %s", sourceCode.getInputFile().filename()));

        Set<Integer> functions = new HashSet<>();
        Set<Integer> statements = new HashSet<>();

        analyzeFunctions(sourceCode.getSourceFile(), functions, statements);

        MetricUtils.saveMeasure(context, sourceCode.getInputFile(), CoreMetrics.FUNCTIONS, functions.size());
        MetricUtils.saveMeasure(context, sourceCode.getInputFile(), CoreMetrics.STATEMENTS, statements.size());
    }

    private static void analyzeFunctions(SourceFile sourceFile, Set<Integer> functions, Set<Integer> statements) {
        analyzeKeywords(sourceFile.getTestCases(), functions, statements);
        analyzeKeywords(sourceFile.getUserKeywords(), functions, statements);
        sourceFile.getVariables().forEach(variable -> statements.add(variable.getPosition().getStartMark().getLine()));
    }

    private static void analyzeKeywords(List<? extends KeywordDefinition> keywords, Set<Integer> functions, Set<Integer> statements){
        for(KeywordDefinition keyword: keywords){
            functions.add(keyword.getPosition().getStartMark().getLine());

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
