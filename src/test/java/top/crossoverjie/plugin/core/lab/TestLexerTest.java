package top.crossoverjie.plugin.core.lab;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class TestLexerTest {

    @Test
    public void tokenize() throws IOException {
        TestLexer lexer = new TestLexer();
        String script = "x = 100" ;
        List<TestLexer.Result> tokenize = lexer.tokenize(script);
        System.out.println("token \t value");
        for (TestLexer.Result result : tokenize) {
            System.out.println(result.tokenType + " \t " + result.text.toString());
        }
    }

    @Test
    public void tokenize2() throws IOException {
        TestLexer lexer = new TestLexer();
        String script = "ab = 10" ;
        List<TestLexer.Result> tokenize = lexer.tokenize(script);
        System.out.println("token \t value");
        for (TestLexer.Result result : tokenize) {
            System.out.println(result.tokenType + " \t " + result.text.toString());
        }
    }
}