package top.crossoverjie.plugin.core;

import com.moilioncircle.ddl.parser.TableElement;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static top.crossoverjie.plugin.core.Constants.CLASS_NAME;
import static top.crossoverjie.plugin.core.Constants.FILED_NAME;
import static top.crossoverjie.plugin.core.Constants.FILED_TYPE;
import static top.crossoverjie.plugin.core.Constants.TABLE_NAME;

public class DDLParseTest {

    @Test
    public void tables() throws IOException {
        String sql = "DROP TABLE IF EXISTS `delivery_fee_plan_info`;\nCREATE TABLE `delivery_fee_plan_info` (\n`id` int(11) NOT NULL AUTO_INCREMENT,\n`city_id` int(11) NOT NULL,\n`name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '名称',\n`start_step_fee` decimal(15,2) NOT NULL COMMENT '起步价',\n`free_distance` int(11) NOT NULL COMMENT '免费配送距离',\n`distance_unit` int(11) NOT NULL COMMENT '计算费用的最小距离单位',\n`is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1删除',\n`create_time` datetime NOT NULL,\n`update_time` datetime NULL,\nPRIMARY P_K (`id`)\n)\nCOMMENT = '配送费方案基本信息表';\n\nDROP TABLE IF EXISTS `delivery_fee_plan_detail_normal`;\nCREATE TABLE `delivery_fee_plan_detail_normal` (\n`id` int(11) NOT NULL AUTO_INCREMENT,\n`plan_id` int(11) NOT NULL,\n`type` int(11) NOT NULL COMMENT '1(正常距离)、2(额外距离)',\n`min_distance` int(11) COMMENT '起始距离',\n`max_distance` int(11) COMMENT '截止距离',\n`fee` decimal(15,4) NOT NULL COMMENT '计费金额，与delivery_fee_plan_info中的distance_unit一起计算，比如：0.2刀每100米',\n`fixed_fee` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '固定费用，默认为0',\nPRIMARY P_K (`id`) ,\nINDEX `idx_plan_id` (`plan_id` ASC) USING BTREE\n)\nCOMMENT = '普通时段运费明细（一个配送方案里普通时段有且只有一条）';";
        List<TableElement> tables = new DDLParse(sql).tables();
        assertEquals(tables.size(), 2);
        for (TableElement table : tables) {
            System.out.println(table.getTableName().getValue());
        }
    }

    @Test
    public void parseModel() {
        String sql = "CREATE TABLE `delivery_fee_plan_special_time_part` (\n`id` int(11) NOT NULL AUTO_INCREMENT,\n`start_time` varchar(20) NOT NULL COMMENT '开始时间',\n`end_time` varchar(20) NOT NULL COMMENT '截止时间',\nPRIMARY P_K (`id`)\n)\nCOMMENT = '特殊时段运费明细（一个配送方案中特殊时段可以 N 条）';";
        DDLParse ddlParse = new DDLParse(sql);
        Map<String, String> metaMapping = new HashMap<>();
        metaMapping.put(CLASS_NAME, ddlParse.toCamelCase("delivery_fee_plan_detail_normal"));
        metaMapping.put(TABLE_NAME, "delivery_fee_plan_detail_normal");
        String meta = ddlParse.transferMeta(metaMapping);
        System.out.println(meta);


        Map<String, String> fieldMapping2 = new HashMap<>();
        fieldMapping2.put(FILED_NAME, "id");
        fieldMapping2.put(FILED_TYPE, "Integer");
        String field2 = ddlParse.transferFiled(fieldMapping2, true) ;
        System.out.println(field2);

        Map<String, String> fieldMapping = new HashMap<>();
        fieldMapping.put(FILED_NAME, "plan_id");
        fieldMapping.put(FILED_TYPE, "Integer");
        String field = ddlParse.transferFiled(fieldMapping, false) ;
        System.out.println(field);


    }

    @Test
    public void transfer1() throws IOException {
        String sql = "CREATE TABLE `delivery_fee_plan_special_time_part` (\n`id` int(11) NOT NULL AUTO_INCREMENT,\n`start_time` varchar(20) NOT NULL COMMENT '开始时间',\n`end_time` varchar(20) NOT NULL COMMENT '截止时间',\nPRIMARY P_K (`id`)\n)\nCOMMENT = '特殊时段运费明细（一个配送方案中特殊时段可以 N 条）';";
        DDLParse ddlParse = new DDLParse(sql);
        System.out.println(ddlParse.transfer()) ;

    }

    @Test
    public void transfer2() throws IOException {
        String sql = "DROP TABLE IF EXISTS `delivery_fee_plan_info`;\nCREATE TABLE `delivery_fee_plan_info` (\n`id` int(11) NOT NULL AUTO_INCREMENT,\n`city_id` int(11) NOT NULL,\n`name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '名称',\n`start_step_fee` decimal(15,2) NOT NULL COMMENT '起步价',\n`free_distance` int(11) NOT NULL COMMENT '免费配送距离',\n`distance_unit` int(11) NOT NULL COMMENT '计算费用的最小距离单位',\n`is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1删除',\n`create_time` datetime NOT NULL,\n`update_time` datetime NULL,\nPRIMARY P_K (`id`)\n)\nCOMMENT = '配送费方案基本信息表';";
        DDLParse ddlParse = new DDLParse(sql);
        System.out.println(ddlParse.transfer()) ;

    }
}