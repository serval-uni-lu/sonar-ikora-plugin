package lu.uni.serval.ikora.sonar.metrics;

import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Tokens;
import lu.uni.serval.ikora.sonar.IkoraSourceCode;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.LinkedList;
import java.util.List;

public class CpdAnalyzer {
    private static final Logger LOGGER = Loggers.get(CpdAnalyzer.class);

    private CpdAnalyzer(){}

    public static void analyse(SensorContext context, IkoraSourceCode sourceCode){
        LOGGER.debug(String.format("Collect tokens in %s", sourceCode.getInputFile().filename()));

        final NewCpdTokens cpdTokens = context.newCpdTokens().onFile(sourceCode.getInputFile());
        final List<Token> tokens = cleanTokens(sourceCode.getSourceFile().getTokens());

        for(Token token: tokens) {
            cpdTokens.addToken(token.getLine(), token.getStartOffset(), token.getLine(), token.getEndOffset(), token.getText());
        }

        cpdTokens.save();
    }

    private static List<Token> cleanTokens(Tokens tokens){
        final LinkedList<Token> clean = new LinkedList<>();

        for(Token current: tokens){
            if(!clean.isEmpty()) {
                final Token previous = clean.getLast();
                if (previous.getLine() == current.getLine() && previous.getEndOffset() >= current.getStartOffset()) {
                    continue;
                }
            }

            clean.add(current);
        }

        return clean;
    }
}
