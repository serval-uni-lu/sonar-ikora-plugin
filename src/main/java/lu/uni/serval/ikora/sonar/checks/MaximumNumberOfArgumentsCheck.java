package lu.uni.serval.ikora.sonar.checks;

import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.UserKeyword;
import lu.uni.serval.ikora.sonar.IkoraLanguage;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

@Rule(key = MaximumNumberOfArgumentsCheck.RULE_KEY)
public class MaximumNumberOfArgumentsCheck extends IkoraLintCheck {
    public static final String RULE_KEY = "MaximumNumberOfArgumentsCheck";

    private static final Logger LOG = Loggers.get(MaximumNumberOfArgumentsCheck.class);

    @Override
    public void validate() {
        super.validate();

        SourceFile sourceFile = ikoraSourceCode.getSourceFile();

        for(UserKeyword userKeyword: sourceFile.getUserKeywords()){
            checkNumberOfArguments(userKeyword);
        }
    }

    private void checkNumberOfArguments(UserKeyword userKeyword) {
        int maxArgs = getInt(IkoraLanguage.MAXIMUM_NUMBER_ARGS, 4);

        if(userKeyword.getParameters().size() > maxArgs){
            LOG.debug(String.format("Add too many arguments issue for '%s'", userKeyword));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    "User keyword should have a maximum of 4 arguments",
                    userKeyword.getNameToken().getLine(),
                    userKeyword.getNameToken().getEndOffset()
            );

            ikoraSourceCode.addViolation(issue);
        }
    }
}
