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
