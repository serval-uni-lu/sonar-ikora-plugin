package lu.uni.serval.ikora.sonar.metrics;

import lu.uni.serval.ikora.core.analytics.clones.CloneCluster;
import lu.uni.serval.ikora.core.analytics.clones.Clones;
import lu.uni.serval.ikora.core.analytics.clones.KeywordCloneDetection;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.sonar.IkoraSourceCode;
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

        final Set<Integer> cloneLines = new HashSet<>();

        final Project project = sourceCode.getSourceFile().getProject();
        final Clones<KeywordDefinition> clones = KeywordCloneDetection.findClones(project);
        analyzeClones(clones, cloneLines);

        FileLinesContext fileLinesContext = fileLinesContextFactory.createFor(sourceCode.getInputFile());
        cloneLines.forEach(clone -> fileLinesContext.setIntValue(CoreMetrics.DUPLICATED_LINES_KEY, clone, 1));
        fileLinesContext.save();

        MetricUtils.saveMeasure(context, sourceCode.getInputFile(), CoreMetrics.DUPLICATED_LINES, cloneLines.size());
    }

    private static void analyzeClones(Clones<KeywordDefinition> clones, Set<Integer> cloneLines){
        for(CloneCluster<KeywordDefinition> cluster: clones.getClusters()){
            for(KeywordDefinition clone: cluster){
                for(int line = clone.getRange().getStart().getLine(); line < clone.getRange().getEnd().getLine(); ++line){
                    cloneLines.add(line);
                }
            }
        }
    }
}
