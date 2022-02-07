package lu.uni.serval.ikora.sonar;

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

import lu.uni.serval.ikora.core.BuildConfiguration;
import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.error.Errors;
import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.sonar.rules.RulesRepository;
import lu.uni.serval.ikora.sonar.rules.IkoraRule;
import lu.uni.serval.ikora.sonar.rules.IkoraIssue;
import lu.uni.serval.ikora.sonar.metrics.CpdAnalyzer;
import lu.uni.serval.ikora.sonar.metrics.FunctionAnalyzer;
import lu.uni.serval.ikora.sonar.metrics.LineAnalyzer;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public class IkoraSensor implements Sensor {
    private static final Logger LOG = Loggers.get(IkoraSensor.class);

    private final Checks<Object> checks;
    private final FileLinesContextFactory fileLinesContextFactory;

    public IkoraSensor(CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory) {
        this.fileLinesContextFactory = fileLinesContextFactory;

        this.checks = checkFactory
                .create(IkoraLanguage.REPOSITORY_KEY)
                .addAnnotatedChecks(RulesRepository.getRuleClasses());
    }


    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor
                .onlyOnLanguage(IkoraLanguage.KEY)
                .name("Robot Framework Sensor");
    }

    @Override
    @ParametersAreNonnullByDefault
    public void execute(SensorContext context) {
        List<InputFile> inputFiles = getInputFiles(context);
        if (inputFiles.isEmpty()) return;

        final BuildResult result = buildProject(context);

        for(InputFile inputFile: inputFiles){
            SourceFile sourceFile = result.getSourceFile(inputFile.uri());

            if(sourceFile == null) {
                LOG.warn(String.format("Skipped file: %s", inputFile.uri().toString()));
                continue;
            }

            LOG.info(String.format("Analyzing file: %s", inputFile.filename()));

            Errors errors = result.getErrors().in(sourceFile.getSource());
            IkoraSourceCode sourceCode = new IkoraSourceCode(inputFile, sourceFile, errors);

            computeMetrics(context, sourceCode);
            runChecks(context, sourceCode);
        }
    }

    private List<InputFile> getInputFiles(SensorContext context) {
        List<InputFile> inputFiles = new ArrayList<>();

        FilePredicate mainFilesPredicate = context.fileSystem().predicates().and(
                context.fileSystem().predicates().hasType(InputFile.Type.MAIN),
                context.fileSystem().predicates().hasLanguage(IkoraLanguage.KEY)
        );

        context.fileSystem().inputFiles(mainFilesPredicate).forEach(inputFiles::add);

        return inputFiles;
    }

    private BuildResult buildProject(SensorContext context) {
        LOG.info("Start building projects...");

        final BuildResult result = Builder.build(context.fileSystem().baseDir(), new BuildConfiguration(), true);

        LOG.info(String.format(
                "Built projects in %d ms [parsing: %d ms; linking: %d ms]",
                result.getBuildTime(),
                result.getParsingTime(),
                result.getResolveTime()
        ));

        return result;
    }

    private void computeMetrics(SensorContext context, IkoraSourceCode sourceCode) {
        LineAnalyzer.analyse(context, fileLinesContextFactory, sourceCode);
        FunctionAnalyzer.analyse(context, sourceCode);
        CpdAnalyzer.analyse(context, sourceCode);
    }

    private void runChecks(SensorContext context, IkoraSourceCode sourceCode){
        for(Object object: checks.all()){
            IkoraRule check = (IkoraRule)object;

            check.setRuleKey(checks.ruleKey(check));
            check.setIkoraSourceCode(sourceCode);
            check.setContext(context);

            LOG.debug(String.format("Checking rule: %s", check.getRuleKey()));
            check.validate();
        }

        saveIssues(context, sourceCode);
    }

    private void saveIssues(SensorContext context, IkoraSourceCode sourceCode){
        LOG.debug(String.format("Issues found: %d", sourceCode.getIkoraIssues().size()));

        for(IkoraIssue issue: sourceCode.getIkoraIssues()){
            LOG.debug(String.format("Save issue: %s", issue.getMessage()));

            NewIssue newIssue = context.newIssue().forRule(issue.getRuleKey());
            NewIssueLocation location = newIssue.newLocation()
                    .on(sourceCode.getInputFile())
                    .message(issue.getMessage())
                    .at(issue.getTextRange());

            newIssue.at(location).save();
        }
    }
}
