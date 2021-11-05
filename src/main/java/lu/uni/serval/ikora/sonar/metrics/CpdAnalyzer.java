package lu.uni.serval.ikora.sonar.metrics;

import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.sonar.IkoraSourceCode;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class CpdAnalyzer {
    private static final Logger LOGGER = Loggers.get(CpdAnalyzer.class);

    private CpdAnalyzer(){}

    public static void analyse(SensorContext context, IkoraSourceCode sourceCode){
        LOGGER.debug(String.format("Collect tokens in %s", sourceCode.getInputFile().filename()));

        NewCpdTokens cpdTokens = context.newCpdTokens().onFile(sourceCode.getInputFile());

        for(Token token: sourceCode.getSourceFile().getTokens()) {
            cpdTokens.addToken(token.getLine(), token.getStartOffset(), token.getLine(), token.getEndOffset(), token.getText());
        }

        cpdTokens.save();
    }
}
