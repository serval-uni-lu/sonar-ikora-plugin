package org.ikora;

import org.ikora.checks.IkoraIssue;
import org.ikora.model.SourceFile;
import org.sonar.api.batch.fs.InputFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IkoraSourceCode {
    private final List<IkoraIssue> ikoraIssues = new ArrayList<>();
    private IkoraIssue syntaxError = null;

    private InputFile inputFile;
    private SourceFile sourceFile;
    private String content = null;

    public IkoraSourceCode(InputFile ikoraFile, SourceFile sourceFile) {
        this.inputFile = ikoraFile;
        this.sourceFile = sourceFile;
    }

    public void addViolation(IkoraIssue issue){
        this.ikoraIssues.add(issue);

        if(issue.isSyntaxError() && syntaxError == null){
            syntaxError = issue;
        }
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

    public boolean hasCorrectSyntax() {
        return syntaxError == null;
    }

    public List<IkoraIssue> getIkoraIssues() {
        return ikoraIssues;
    }

    public IkoraIssue getSyntaxError() {
        return syntaxError;
    }
}
