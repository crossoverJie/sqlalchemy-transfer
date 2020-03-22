package top.crossoverjie.plugin.core;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class SqlLexerTest {

    @Test
    public void tokenize() throws IOException {
        SqlLexer lexer = new SqlLexer();
        String sql = "CREATE TABLE `T` (\n`id` int(11) NOT NULL AUTO_INCREMENT\nPRIMARY P_K (`id`)\n)\nCOMMENT = '默认表';";
        List<SqlLexer.TokenResult> tokenize = lexer.tokenize(sql,0);
        for (SqlLexer.TokenResult result : tokenize) {
            System.out.println(result.getTokenType() + "\t" + result.getText().toString());
        }
    }

    @Test
    public void tokenize2() throws IOException {
        SqlLexer lexer = new SqlLexer();
        String sql = "CREATE TABLE `t_info` (\n  `name` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '名称',\n  PRIMARY KEY (`id`)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='信息表'";
        List<SqlLexer.TokenResult> tokenize = lexer.tokenize(sql,0);
        for (SqlLexer.TokenResult result : tokenize) {
            System.out.println(result.pid() + "\t" + result.getTokenType() + "\t" + result.getText().toString());
        }
    }

    @Test
    public void tokenize3() throws IOException {
        SqlLexer lexer = new SqlLexer();
        String sql = "PRIMARY KEY (`id`)";
        List<SqlLexer.TokenResult> tokenize = lexer.tokenize(sql,1);
        for (SqlLexer.TokenResult result : tokenize) {
            System.out.println(result.getTokenType() + "\t" + result.getText().toString());
        }
    }
    @Test
    public void tokenize4() throws IOException {
        SqlLexer lexer = new SqlLexer();
        String sql = "(`id`)";
        List<SqlLexer.TokenResult> tokenize = lexer.tokenize(sql,2);
        for (SqlLexer.TokenResult result : tokenize) {
            System.out.println(result.getTokenType() + "\t" + result.getText().toString());
        }
    }
    @Test
    public void tokenize5() throws IOException {
        SqlLexer lexer = new SqlLexer();
        String sql = "`id`";
        List<SqlLexer.TokenResult> tokenize = lexer.tokenize(sql,3);
        for (SqlLexer.TokenResult result : tokenize) {
            System.out.println(result.getTokenType() + "\t" + result.getText().toString());
        }
    }
}