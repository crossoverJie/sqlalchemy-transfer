package top.crossoverjie.plugin.core;

import org.junit.Test;
import top.crossoverjie.plugin.core.parse.DDLInfo;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DDLParseTest {


    @Test
    public void generateDDLInfo2() throws IOException {
        String sql = "CREATE TABLE `user` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `userName` varchar(20) DEFAULT NULL COMMENT '用户名',\n" +
                "  `password` varchar(100) DEFAULT NULL COMMENT '密码',\n" +
                "  `roleId` int(11) DEFAULT NULL COMMENT '角色ID',\n" +
                "  PRIMARY KEY (`id`),  \n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8";
        List<DDLInfo> ddlInfoList = new DDLParse(sql).generateDDLInfo();
        assertEquals(ddlInfoList.size(), 1);
        assertEquals(ddlInfoList.get(0).getTableName(), "user");
        assertEquals(ddlInfoList.get(0).getPrimaryKey(), "id");

    }


}