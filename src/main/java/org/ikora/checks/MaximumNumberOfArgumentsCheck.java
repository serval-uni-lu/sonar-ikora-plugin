package org.ikora.checks;

import org.ikora.model.SourceFile;
import org.ikora.model.UserKeyword;
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
        if(userKeyword.getParameters().size() > 4){
            LOG.debug(String.format("Too many arguments for user keyword '%s'", userKeyword.getName()));

            IkoraIssue issue = new IkoraIssue(ruleKey,
                    "User keyword should have a maximum of 4 arguments",
                    userKeyword.getPosition().getStartMark().getLine(),
                    userKeyword.getPosition().getStartMark().getColumn());

            ikoraSourceCode.addViolation(issue);
        }
    }
}
