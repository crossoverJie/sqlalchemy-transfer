package top.crossoverjie.plugin.core;

import org.junit.Test;
import top.crossoverjie.plugin.core.parse.DDLInfo;
import top.crossoverjie.plugin.core.parse.DDLTokenType;
import top.crossoverjie.plugin.core.parse.FieldInfo;
import top.crossoverjie.plugin.core.parse.StandardDDLLexer;
import top.crossoverjie.plugin.core.parse.Status;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandardDDLLexerTest {


    @Test
    public void tokenize100() throws IOException {
        DDLInfo ddlInfo = new DDLInfo();
        StandardDDLLexer lexer = new StandardDDLLexer();
        String sql = "CREATE TABLE `user` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `userName` varchar(20) DEFAULT NULL COMMENT '用户名',\n" +
                "  `password` varchar(100) DEFAULT NULL COMMENT '密码',\n" +
                "  `roleId` int(11) DEFAULT NULL COMMENT '角色ID',\n" +
                "  PRIMARY KEY (`id`),  \n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8";
        List<StandardDDLLexer.TokenResult> tokenize = lexer.tokenize(sql, Status.BASE_INIT, 0);
        System.out.println("base \ttoken-type \t value \t pid");

        List<Integer> fi = new ArrayList<>();
        Map<Integer, List<StandardDDLLexer.TokenResult>> childInfoMapping = new HashMap<>();
        List<StandardDDLLexer.TokenResult> tokenResultList = new ArrayList<>();
        for (StandardDDLLexer.TokenResult result : tokenize) {
            System.out.println(result.status() + "\t" + result.getTokenType() + "\t" + result.getText().toString() + "\t" + result.getPid());
            if (result.getTokenType() == DDLTokenType.FI) {
                fi.add(result.getPid());
            }

            if (result.status() == Status.BASE_INIT) {
                tokenResultList.add(result);
            }


            if (result.getTokenType() == DDLTokenType.TBN) {
                ddlInfo.setTableName(result.getText().toString());
            }

            if (result.getTokenType() == DDLTokenType.P_K_V) {
                ddlInfo.setPrimaryKey(result.getText().toString());
            }
        }

        for (Integer pid : fi) {
            List<StandardDDLLexer.TokenResult> resultList = new ArrayList<>();
            for (StandardDDLLexer.TokenResult tokenResult : tokenResultList) {
                if (tokenResult.getPid() == pid) {
                    resultList.add(tokenResult);
                }
            }
            childInfoMapping.put(pid, resultList);

        }


        List<FieldInfo> fieldInfos = new ArrayList<>();

        for (Map.Entry<Integer, List<StandardDDLLexer.TokenResult>> entry : childInfoMapping.entrySet()) {
            List<StandardDDLLexer.TokenResult> tokenResults = entry.getValue();
            FieldInfo info = new FieldInfo();
            for (StandardDDLLexer.TokenResult result : tokenResults) {
                if (result.getTokenType() == DDLTokenType.FIELD_NAME) {
                    info.setFieldName(result.getText().toString());
                }
                if (result.getTokenType() == DDLTokenType.FIELD_TYPE) {
                    info.setFiledType(result.getText().toString().trim());
                }
                if (result.getTokenType() == DDLTokenType.FIELD_LEN) {
                    info.setFieldLen(result.getText().toString());
                }
                if (result.getTokenType() == DDLTokenType.FIELD_COMMENT) {
                    info.setComment(result.getText().toString());
                }
            }
            fieldInfos.add(info);

        }

        System.out.println("==========");
        ddlInfo.setFieldInfos(fieldInfos);
        System.out.println(ddlInfo.toString());

    }

    @Test
    public void tokenize2() throws IOException {
        StandardDDLLexer lexer = new StandardDDLLexer();
        String sql = "`name` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT ";
        List<StandardDDLLexer.TokenResult> tokenize = lexer.tokenize(sql, Status.BASE_FIELD_TYPE, 0);
        System.out.println("base \ttoken-type \t value");
        for (StandardDDLLexer.TokenResult result : tokenize) {
            System.out.println(result.status() + "\t" + result.getTokenType() + "\t" + result.getText().toString());
        }
    }

    @Test
    public void tokenize4() throws IOException {
        StandardDDLLexer lexer = new StandardDDLLexer();
        String sql = "`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',";
        List<StandardDDLLexer.TokenResult> tokenize = lexer.tokenize(sql, Status.BASE_FIELD_TYPE, 0);
        System.out.println("base \ttoken-type \t value");
        for (StandardDDLLexer.TokenResult result : tokenize) {
            System.out.println(result.status() + "\t" + result.getTokenType() + "\t" + result.getText().toString());
        }
    }

    @Test
    public void tokenize3() throws IOException {
        StandardDDLLexer lexer = new StandardDDLLexer();
        String sql = "`name` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '名称'";
        List<StandardDDLLexer.TokenResult> tokenize = lexer.tokenize(sql, Status.BASE_FIELD_COMMENT, 0);
        System.out.println("base \ttoken-type \t value");
        for (StandardDDLLexer.TokenResult result : tokenize) {
            System.out.println(result.status() + "\t" + result.getTokenType() + "\t" + result.getText().toString());
        }
    }

    @Test
    public void tokenize5() throws IOException {
        StandardDDLLexer lexer = new StandardDDLLexer();
        String sql = "`name` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '名称'";
        List<StandardDDLLexer.TokenResult> tokenize = lexer.tokenize(sql, Status.BASE_FIELD_LEN, 0);
        System.out.println("base \ttoken-type \t value");
        for (StandardDDLLexer.TokenResult result : tokenize) {
            System.out.println(result.status() + "\t" + result.getTokenType() + "\t" + result.getText().toString());
        }
    }

    @Test
    public void tokenize6() throws IOException {
        StandardDDLLexer lexer = new StandardDDLLexer();
        String sql = "  KEY `idx_order_id` (`order_id`) USING BTREE";
        List<StandardDDLLexer.TokenResult> tokenize = lexer.tokenize(sql, Status.BASE_INIT, 0);
        System.out.println("base \ttoken-type \t value \t pid");
        for (StandardDDLLexer.TokenResult result : tokenize) {
            System.out.println(result.status() + "\t" + result.getTokenType() + "\t" + result.getText().toString() + "\t" + result.getPid());
        }
    }



    @Test
    public void tokenize8() throws IOException {
        StandardDDLLexer lexer = new StandardDDLLexer();
        String sql = "CREATE TABLE `user` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `userName` varchar(20) DEFAULT NULL COMMENT '用户名',\n" +
                "  `password` varchar(100) DEFAULT NULL COMMENT '密码',\n" +
                "  `roleId` int(11) DEFAULT NULL COMMENT '角色ID',\n" +
                "  PRIMARY KEY (`id`),  \n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8";
        List<StandardDDLLexer.TokenResult> tokenize = lexer.tokenize(sql, Status.BASE_INIT, 0);
        System.out.println("base \ttoken-type \t value \t pid");
        for (StandardDDLLexer.TokenResult result : tokenize) {
            System.out.println(result.status() + "\t" + result.getTokenType() + "\t" + result.getText().toString() + "\t" + result.getPid());
        }
    }


    @Test
    public void tokenize10() throws IOException {
        StandardDDLLexer lexer = new StandardDDLLexer();
        String sql = "CREATE TABLE `xx_fee` (\n" +
                "  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,\n" +
                "  `xx1_id` int(11) unsigned NOT NULL,\n" +
                "  `xx2_id` int(11) unsigned DEFAULT '0',\n" +
                "  `xx3_id` int(11) NOT NULL DEFAULT '0',\n" +
                "  `xx4_status` varchar(64) NOT NULL DEFAULT 'available' COMMENT 'xx状态',\n" +
                "  `xx5_fee` decimal(15,2) DEFAULT '0.00' COMMENT 'xx最小',\n" +
                "  `xx6_fee` decimal(15,2) DEFAULT '0.00' COMMENT 'xx最大',\n" +
                "  `create_time` varchar(20) NOT NULL DEFAULT '',\n" +
                "  `update_time` varchar(20) DEFAULT '',\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='xx表';";
        List<StandardDDLLexer.TokenResult> tokenize = lexer.tokenize(sql, Status.BASE_INIT, 0);
        System.out.println("base \ttoken-type \t value \t pid");
        for (StandardDDLLexer.TokenResult result : tokenize) {
            System.out.println(result.status() + "\t" + result.getTokenType() + "\t" + result.getText().toString() + "\t" + result.getPid());
        }

        String transfer = new DDLParse(sql).transfer();
        System.out.println(transfer);
    }

    @Test
    public void tokenize11() throws IOException {
        StandardDDLLexer lexer = new StandardDDLLexer();
        String sql = "CREATE TABLE `xx_fee` (\n" +
                "  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,\n" +
                "  `xx4_status` varchar(64) NOT NULL COMMENT '我 HH 态',\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='xx表';";
        List<StandardDDLLexer.TokenResult> tokenize = lexer.tokenize(sql, Status.BASE_INIT, 0);
        System.out.println("base \ttoken-type \t value \t pid");
        for (StandardDDLLexer.TokenResult result : tokenize) {
            System.out.println(result.status() + "\t" + result.getTokenType() + "\t" + result.getText().toString() + "\t" + result.getPid());
        }

        String transfer = new DDLParse(sql).transfer();
        System.out.println(transfer);
    }

    @Test
    public void tokenize12() throws IOException {
        StandardDDLLexer lexer = new StandardDDLLexer();
        String sql = "CREATE TABLE `xx_fee` (\n" +
                "  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,\n" +
                "  `xx4_status` varchar(64) NOT NULL COMMENT '时间，多个用（|）分割',\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='xx表';";
        List<StandardDDLLexer.TokenResult> tokenize = lexer.tokenize(sql, Status.BASE_INIT, 0);
        System.out.println("base \ttoken-type \t value \t pid");
        for (StandardDDLLexer.TokenResult result : tokenize) {
            System.out.println(result.status() + "\t" + result.getTokenType() + "\t" + result.getText().toString() + "\t" + result.getPid());
        }

        String transfer = new DDLParse(sql).transfer();
        System.out.println(transfer);
    }

    @Test
    public void tokenize13() throws IOException {
        StandardDDLLexer lexer = new StandardDDLLexer();
        String sql = "CREATE TABLE `table1` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',\n" +
                "  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,\n" +
                "  `update_time` datetime NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='table1';\n" +
                "\n" +
                "CREATE TABLE `table2` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',\n" +
                "  `name` varchar(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '名称',\n" +
                "  PRIMARY KEY (`id`),\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='table2';";
        List<StandardDDLLexer.TokenResult> tokenize = lexer.tokenize(sql, Status.BASE_INIT, 0);
        System.out.println("base \ttoken-type \t value \t pid");
        for (StandardDDLLexer.TokenResult result : tokenize) {
            System.out.println(result.status() + "\t" + result.getTokenType() + "\t" + result.getText().toString() + "\t" + result.getPid());
        }

        String transfer = new DDLParse(sql).transfer();
        System.out.println(transfer);
    }

    @Test
    public void tokenize14() throws IOException {
        StandardDDLLexer lexer = new StandardDDLLexer();
        String sql = "CREATE TABLE `t` (\n" +
                "  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,\n" +
                "  PRIMARY KEY (`id`),\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='table1';\n" ;
        List<StandardDDLLexer.TokenResult> tokenize = lexer.tokenize(sql, Status.BASE_INIT, 0);
        System.out.println("base \ttoken-type \t value \t pid");
        for (StandardDDLLexer.TokenResult result : tokenize) {
            System.out.println(result.status() + "\t" + result.getTokenType() + "\t" + result.getText().toString() + "\t" + result.getPid());
        }

        String transfer = new DDLParse(sql).transfer();
        System.out.println(transfer);
    }


    @Test
    public void str() throws IOException {

        char a = 'A' ;

        System.out.println(Character.toLowerCase(a));

        String sql = "`xx4_status` varchar(64) NOT NULL COMMENT '我 HH 态'" ;
        CharArrayReader reader = new StandardDDLLexer.CustomCharArrayReader(sql.toCharArray());
        int pre = ((StandardDDLLexer.CustomCharArrayReader) reader).preReadNext();
        System.out.println((char) pre);
        int ch;
        char value;
        while ((ch = reader.read()) != -1) {
            value = (char) ch;
            System.out.println((char) ch);
            if (value == 'v'){
                System.out.println("=====" + (char)((StandardDDLLexer.CustomCharArrayReader) reader).frontRead(2));
            }
        }


    }

}