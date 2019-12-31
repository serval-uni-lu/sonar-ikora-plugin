package org.ikora;

import org.ikora.checks.IkoraIssue;
import org.ikora.error.Errors;
import org.ikora.model.SourceFile;
import org.sonar.api.batch.fs.InputFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IkoraSourceCode {
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
