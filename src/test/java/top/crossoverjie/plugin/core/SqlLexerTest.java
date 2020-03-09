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
        String sql = "CREATE TABLE `delivery_fee_for_courier` (\n`id` int(11) NOT NULL AUTO_INCREMENT,\n`city_id` int(11) NOT NULL,\n`plan_id` int(11) NOT NULL COMMENT '配送费方案ID',\n`area_id` int(11) NOT NULL DEFAULT 0 COMMENT '区域ID',\n`order_delivery_type` int(11) NOT NULL COMMENT '订单类型（1/2/3)普通订单、代购订单、私人订单',\n`drivering_type` int(11) NOT NULL DEFAULT 0 COMMENT '载具类型 0 通用载具 详见常量：COURIER_DRIVING_TYPE',\n`status` int(11) NOT NULL COMMENT '开关状态 0关闭，1开启',\n`is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1删除',\n`create_time` datetime NOT NULL,\n`update_time` datetime NULL,\nPRIMARY KEY (`id`)\n)\nCOMMENT = '面向配送员的配送费方案';";
        List<SqlLexer.TokenResult> tokenize = lexer.tokenize(sql);
        for (SqlLexer.TokenResult result : tokenize) {
            System.out.println(result.getTokenType() + "\t" + result.getText().toString());
        }
    }
}