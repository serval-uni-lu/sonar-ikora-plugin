package lu.uni.serval.ikora.sonar;

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
