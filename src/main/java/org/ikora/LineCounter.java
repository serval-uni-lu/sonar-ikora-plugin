package org.ikora;

import org.ikora.builder.Line;
import org.ikora.model.SourceFile;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.measures.Metric;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class LineCounter {
    private static final Logger LOGGER = Loggers.get(LineCounter.class);

    private LineCounter(){}

    private static <T extends Serializable> void saveMeasure(SensorContext context, InputFile inputFile, Metric<T> metric, T value){
        context.<T>newMeasure()
                .withValue(value)
                .forMetric(metric)
                .on(inputFile)
                .save();
    }

    public static void analyse(SensorContext context, FileLinesContextFactory fileLinesContextFactory, IkoraSourceCode sourceCode){
        LOGGER.debug(String.format("Get lines in %s", sourceCode.getInputFile().filename()));

        Set<Integer> linesOfCode = new HashSet<>();
        Set<Integer> commentLines = new HashSet<>();

        analyzeLines(sourceCode.getSourceFile(), linesOfCode, commentLines);

        FileLinesContext fileLinesContext = fileLinesContextFactory.createFor(sourceCode.getInputFile());
        linesOfCode.forEach(lineOfCode -> fileLinesContext.setIntValue(CoreMetrics.NCLOC_DATA_KEY, lineOfCode, 1));
        fileLinesContext.save();

        saveMeasure(context, sourceCode.getInputFile(), CoreMetrics.NCLOC, linesOfCode.size());
        saveMeasure(context, sourceCode.getInputFile(), CoreMetrics.COMMENT_LINES, commentLines.size());
    }

    private static void analyzeLines(SourceFile sourceFile, Set<Integer> linesOfCode, Set<Integer> commentLines){
        for(Line line: sourceFile.getLines()){
            if(line.isCode()) linesOfCode.add(line.getNumber());
            else if(line.isComment()) commentLines.add(line.getNumber());
        }
    }
}
