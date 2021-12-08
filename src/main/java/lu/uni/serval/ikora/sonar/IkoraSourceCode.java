package lu.uni.serval.ikora.sonar;

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

import lu.uni.serval.ikora.core.error.Errors;
import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.sonar.rules.IkoraIssue;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IkoraSourceCode {
    private static final Logger LOG = Loggers.get(IkoraSourceCode.class);

    private final List<IkoraIssue> ikoraIssues = new ArrayList<>();
    private final InputFile inputFile;
    private final SourceFile sourceFile;
    private final Errors errors;

    private String content = null;

    public IkoraSourceCode(InputFile inputFile, SourceFile sourceFile, Errors errors) {
        this.inputFile = inputFile;
        this.sourceFile = sourceFile;
        this.errors = errors;
    }

    public void addViolation(IkoraIssue issue){
        if(issue.getTextRange().start().line() < 0){
            LOG.warn(String.format("Failed to add issue in file %s :: %s", this.inputFile.filename(), issue));
            return;
        }

        this.ikoraIssues.add(issue);
    }

    public SourceFile getSourceFile() {
        return sourceFile;
    }

    public InputFile getInputFile() {
        return inputFile;
    }

    public String getContent() throws IOException {
        if(content == null){
            this.content = inputFile.contents();
        }

        return content;
    }

    public Errors getErrors() {
        return errors;
    }

    public List<IkoraIssue> getIkoraIssues() {
        return ikoraIssues;
    }
}
