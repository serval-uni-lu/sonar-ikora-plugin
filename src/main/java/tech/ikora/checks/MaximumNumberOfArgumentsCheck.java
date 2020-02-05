package tech.ikora.checks;

import tech.ikora.IkoraLanguage;
import tech.ikora.model.SourceFile;
import tech.ikora.model.UserKeyword;
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
            LOG.debug(String.format("Add too many arguments issue for '%s'", userKeyword.toString()));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    "User keyword should have a maximum of 4 arguments",
                    userKeyword.getPosition().getStartMark().getLine(),
                    userKeyword.getPosition().getStartMark().getColumn());

            ikoraSourceCode.addViolation(issue);
        }
    }
}
