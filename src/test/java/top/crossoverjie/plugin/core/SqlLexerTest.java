package top.crossoverjie.plugin.core;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class SqlLexerTest {

    @Test
    public void tokenize() throws IOException {
        SqlLexer lexer = new SqlLexer();
        String sql = "CREATE TABLE `T` (\n`id` int(11) NOT NULL AUTO_INCREMENT\nPRIMARY KEY (`id`)\n)\nCOMMENT = '默认配送费表';";
        List<SqlLexer.TokenResult> tokenize = lexer.tokenize(sql);
        for (SqlLexer.TokenResult result : tokenize) {
            System.out.println(result.getTokenType() + "\t" + result.getText().toString());
        }
    }

    @Test
    public void tokenize2() throws IOException {
        SqlLexer lexer = new SqlLexer();
        String sql = "CREATE TABLE `open_api_terminal_info` (\n  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',\n  `name` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '终端机名称',\n  `clazz` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '业务实现类',\n  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,\n  PRIMARY KEY (`id`)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商家终端机信息表'";
        List<SqlLexer.TokenResult> tokenize = lexer.tokenize(sql);
        for (SqlLexer.TokenResult result : tokenize) {
            System.out.println(result.getTokenType() + "\t" + result.getText().toString());
        }
    }
}