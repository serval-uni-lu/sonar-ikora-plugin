package lu.uni.serval.ikora.sonar.metrics;

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

import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Tokens;
import lu.uni.serval.ikora.sonar.IkoraSourceCode;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    static List<Token> cleanTokens(Tokens tokens){
        final LinkedList<Token> clean = new LinkedList<>();

        for(Token current: tokens.asList().stream().sorted().collect(Collectors.toList())){
            if(clean.isEmpty()) {
                clean.add(current);
            }
            else{
                final Token previous = clean.getLast();

                if(!intersects(previous, current)){
                    clean.add(current);
                }
                else if(isGreater(current, previous)) {
                    clean.removeLast();
                    clean.add(current);
                }
            }
        }

        return clean;
    }

    static boolean intersects(Token token1, Token token2) {
        return token2.getEndOffset() >= token1.getStartOffset() && token2.getStartOffset() <= token1.getEndOffset();
    }

    static boolean isGreater(Token token1, Token token2) {
        return token1.getStartOffset() <= token2.getStartOffset() && token1.getEndOffset() >= token2.getEndOffset();
    }
}
