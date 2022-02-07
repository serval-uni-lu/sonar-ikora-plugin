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
