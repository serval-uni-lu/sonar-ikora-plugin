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

import lu.uni.serval.ikora.core.builder.parser.Line;
import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.sonar.IkoraSourceCode;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.HashSet;
import java.util.Set;

public class LineAnalyzer {
    private static final Logger LOGGER = Loggers.get(LineAnalyzer.class);

    private LineAnalyzer(){}

    public static void analyse(SensorContext context, FileLinesContextFactory fileLinesContextFactory, IkoraSourceCode sourceCode){
        LOGGER.debug(String.format("Get lines in %s", sourceCode.getInputFile().filename()));

        Set<Integer> linesOfCode = new HashSet<>();
        Set<Integer> commentLines = new HashSet<>();

        analyzeLines(sourceCode.getSourceFile(), linesOfCode, commentLines);

        FileLinesContext fileLinesContext = fileLinesContextFactory.createFor(sourceCode.getInputFile());
        linesOfCode.forEach(lineOfCode -> fileLinesContext.setIntValue(CoreMetrics.NCLOC_DATA_KEY, lineOfCode, 1));
        fileLinesContext.save();

        MetricUtils.saveMeasure(context, sourceCode.getInputFile(), CoreMetrics.NCLOC, linesOfCode.size());
        MetricUtils.saveMeasure(context, sourceCode.getInputFile(), CoreMetrics.COMMENT_LINES, commentLines.size());
    }

    private static void analyzeLines(SourceFile sourceFile, Set<Integer> linesOfCode, Set<Integer> commentLines){
        for(Line line: sourceFile.getLines()){
            if(line.isCode()) linesOfCode.add(line.getNumber());
            else if(line.isComment()) commentLines.add(line.getNumber());
        }
    }
}
