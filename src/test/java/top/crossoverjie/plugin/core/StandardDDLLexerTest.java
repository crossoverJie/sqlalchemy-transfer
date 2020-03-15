package top.crossoverjie.plugin.core;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class StandardDDLLexerTest {

    @Test
    public void tokenize() throws IOException {
        StandardDDLLexer lexer = new StandardDDLLexer();
        String sql = "CREATE TABLE `open_api_terminal_info` (\n  `name` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '终端机名称',\n  PRIMARY KEY (`id`)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商家终端机信息表'";
        List<StandardDDLLexer.TokenResult> tokenize = lexer.tokenize(sql,Status.BASE_INIT);
        System.out.println("base \ttoken-type \t value");
        for (StandardDDLLexer.TokenResult result : tokenize) {
            System.out.println(result.status() + "\t" + result.getTokenType() + "\t" + result.getText().toString());
        }
    }
    @Test
    public void tokenize1() throws IOException {
        StandardDDLLexer lexer = new StandardDDLLexer();
        String sql = "CREATE TABLE `open_api_terminal_info` (\n  `name` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '终端机名称',\n  `clazz` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '自定义实现类',\n  PRIMARY KEY (`id`)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商家终端机信息表'";
        List<StandardDDLLexer.TokenResult> tokenize = lexer.tokenize(sql,Status.BASE_INIT);
        System.out.println("base \ttoken-type \t value");
        for (StandardDDLLexer.TokenResult result : tokenize) {
            System.out.println(result.status() + "\t" + result.getTokenType() + "\t" + result.getText().toString());
        }
    }

    @Test
    public void tokenize2() throws IOException {
        StandardDDLLexer lexer = new StandardDDLLexer();
        String sql = "`name` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT ";
        List<StandardDDLLexer.TokenResult> tokenize = lexer.tokenize(sql,Status.BASE_FIELD_TYPE);
        System.out.println("base \ttoken-type \t value");
        for (StandardDDLLexer.TokenResult result : tokenize) {
            System.out.println(result.status() + "\t" + result.getTokenType() + "\t" + result.getText().toString());
        }
    }

    @Test
    public void tokenize3() throws IOException {
        StandardDDLLexer lexer = new StandardDDLLexer();
        String sql = "`name` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '终端机名称'";
        List<StandardDDLLexer.TokenResult> tokenize = lexer.tokenize(sql,Status.BASE_FIELD_COMMENT);
        System.out.println("base \ttoken-type \t value");
        for (StandardDDLLexer.TokenResult result : tokenize) {
            System.out.println(result.status() + "\t" + result.getTokenType() + "\t" + result.getText().toString());
        }
    }
}