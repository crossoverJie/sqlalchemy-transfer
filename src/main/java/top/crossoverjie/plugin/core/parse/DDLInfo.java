package top.crossoverjie.plugin.core.parse;

import java.util.List;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020-03-16 00:48
 * @since JDK 1.8
 */
public class DDLInfo {
    private String tableName;
    private String primaryKey;
    private List<FieldInfo> fieldInfos ;

    public List<FieldInfo> getFieldInfos() {
        return fieldInfos;
    }

    public void setFieldInfos(List<FieldInfo> fieldInfos) {
        this.fieldInfos = fieldInfos;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public String toString() {
        return "DDLInfo{" +
                "tableName='" + tableName + '\'' +
                ", primaryKey='" + primaryKey + '\'' +
                ", fieldInfos=" + fieldInfos +
                '}';
    }
}
