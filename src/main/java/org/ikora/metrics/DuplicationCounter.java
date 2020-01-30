package org.ikora.metrics;

import org.ikora.IkoraSourceCode;
import org.ikora.analytics.Clone;
import org.ikora.analytics.CloneDetection;
import org.ikora.analytics.Clones;
import org.ikora.model.*;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.HashSet;
import java.util.Set;

public class DuplicationCounter {
    private static final Logger LOGGER = Loggers.get(DuplicationCounter.class);

    private DuplicationCounter(){}

    public static void analyse(SensorContext context, FileLinesContextFactory fileLinesContextFactory, IkoraSourceCode sourceCode){
        LOGGER.debug(String.format("Get duplicated in %s", sourceCode.getInputFile().filename()));

        Set<Integer> cloneLines = new HashSet<>();

        Project project = sourceCode.getSourceFile().getProject();
        Clones<UserKeyword> keywordClones = CloneDetection.findClones(project, UserKeyword.class);
        analyzeClones(keywordClones, cloneLines);

        Clones<TestCase> testCaseClones = CloneDetection.findClones(project, TestCase.class);
        analyzeClones(testCaseClones, cloneLines);

        FileLinesContext fileLinesContext = fileLinesContextFactory.createFor(sourceCode.getInputFile());
        cloneLines.forEach(clone -> fileLinesContext.setIntValue(CoreMetrics.DUPLICATED_LINES_KEY, clone, 1));
        fileLinesContext.save();

        MetricUtils.saveMeasure(context, sourceCode.getInputFile(), CoreMetrics.DUPLICATED_LINES, cloneLines.size());
    }

    private static void analyzeClones(Clones<? extends Node> clones, Set<Integer> cloneLines){
        for(Clone<? extends Node> clone: clones){
            Position position = clone.getNode().getPosition();

            for(int line = position.getStartMark().getLine(); line < position.getEndMark().getLine(); ++line){
                cloneLines.add(line);
            }
        }
    }
}
