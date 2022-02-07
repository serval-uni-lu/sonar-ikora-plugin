package lu.uni.serval.ikora.sonar.metrics;

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

import lu.uni.serval.ikora.core.model.KeywordDefinition;
import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.Step;
import lu.uni.serval.ikora.sonar.IkoraSourceCode;
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
        sourceFile.getVariables().forEach(variable -> statements.add(variable.getDefinitionToken().getLine()));
    }

    private static void analyzeKeywords(List<? extends KeywordDefinition> keywords, Set<Integer> functions, Set<Integer> statements){
        for(KeywordDefinition keyword: keywords){
            functions.add(keyword.getDefinitionToken().getLine());

            for(Step step: keyword.getSteps()){
                analyzeSteps(step, statements);
            }
        }
    }

    private static void analyzeSteps(Step step, Set<Integer> statements){
        statements.add(step.getDefinitionToken().getLine());

        for(Step step1: step.getSteps()){
            analyzeSteps(step1, statements);
        }
    }
}
