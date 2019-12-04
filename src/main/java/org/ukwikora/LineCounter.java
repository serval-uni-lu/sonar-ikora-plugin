package org.ukwikora;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.measures.Metric;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.ukwikora.model.SourceFile;

import java.io.Serializable;

public class LineCounter {
    private static final Logger LOGGER = Loggers.get(LineCounter.class);

    private LineCounter(){
    }

    private static <T extends Serializable> void saveMeasure(SensorContext context, InputFile inputFile, Metric<T> metric, T value){
        context.<T>newMeasure()
                .withValue(value)
                .forMetric(metric)
                .on(inputFile)
                .save();
    }

    public static void analyse(SensorContext context, FileLinesContextFactory fileLinesContextFactory, SourceFile sourceFile, InputFile inputFile){
        LOGGER.debug("Get lines in {}", sourceFile.getFile().getAbsolutePath());

        saveMeasure(context, inputFile, CoreMetrics.COMMENT_LINES, sourceFile.getCommentLines());
        saveMeasure(context, inputFile, CoreMetrics.NCLOC, sourceFile.getLinesOfCode());
    }
}
