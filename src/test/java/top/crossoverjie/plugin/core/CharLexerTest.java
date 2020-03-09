package top.crossoverjie.plugin.core;

import groovy.util.logging.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

@Slf4j
public class CharLexerTest {

    @Test
    public void tokenize() throws IOException {
        CharLexer lexer = new CharLexer();
        List<CharLexer.TokenResult> tokenize = lexer.tokenize("age > 200");
        for (CharLexer.TokenResult result : tokenize) {
            System.out.println(result.getTokenType() + "\t" + result.getText().toString());
        }
    }
}