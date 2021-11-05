package lu.uni.serval.ikora.sonar.checks;

import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.UserKeyword;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

@Rule(key = KeywordHasMinimumOneStepCheck.RULE_KEY)
public class KeywordHasMinimumOneStepCheck extends IkoraLintCheck {
    public static final String RULE_KEY = "KeywordHasMinimumOneStepCheck";

    private static final Logger LOG = Loggers.get(KeywordHasMinimumOneStepCheck.class);

    @Override
    public void validate() {
        super.validate();

        SourceFile sourceFile = ikoraSourceCode.getSourceFile();

        for(UserKeyword keyword: sourceFile.getUserKeywords()){
            checkNumberStep(keyword);
        }
    }

    private void checkNumberStep(UserKeyword keyword){
        if(keyword.getSteps().isEmpty()){
            LOG.debug(String.format("Add missing steps issue for '%s'", keyword));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    "Keyword definition needs to have at least one step",
                    keyword.getNameToken().getLine(),
                    keyword.getNameToken().getStartOffset()
            );

            ikoraSourceCode.addViolation(issue);
        }
    }
}
