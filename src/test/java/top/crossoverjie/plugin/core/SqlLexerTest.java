package top.crossoverjie.plugin.core;

import groovy.util.logging.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

@Slf4j
public class SqlLexerTest {

    @Test
    public void tokenize() throws IOException {
        SqlLexer lexer = new SqlLexer();
        List<SqlLexer.TokenResult> tokenize = lexer.tokenize("age > 200");
        for (SqlLexer.TokenResult result : tokenize) {
            System.out.println(result.getTokenType() + "\t" + result.getText().toString());
        }
    }
}