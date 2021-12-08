package lu.uni.serval.ikora.sonar.metrics;

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
