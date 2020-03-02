package top.crossoverjie.plugin.core;

import com.moilioncircle.ddl.parser.ColumnElement;
import com.moilioncircle.ddl.parser.MysqlDDLParser;
import com.moilioncircle.ddl.parser.TableElement;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static top.crossoverjie.plugin.core.Constants.CLASS_NAME;
import static top.crossoverjie.plugin.core.Constants.DB_TYPE_TO_PY;
import static top.crossoverjie.plugin.core.Constants.FILED_NAME;
import static top.crossoverjie.plugin.core.Constants.FILED_TYPE;
import static top.crossoverjie.plugin.core.Constants.TABLE_NAME;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020-02-29 23:29
 * @since JDK 1.8
 */
public class DDLParse {

    private String metaTemplate = "class " + CLASS_NAME + "(db.Model):\n" +
            "    __tablename__ = '" + TABLE_NAME + "'\n";

    private String primaryFieldTemplate = "    " + FILED_NAME + " = db.Column(db." + FILED_TYPE + ", primary_key=True, autoincrement=True" + ")\n";

    private String fieldTemplate = "    " + FILED_NAME + " = db.Column(db." + FILED_TYPE + ")\n";

    private String sql ;

    public DDLParse(String sql) {
        this.sql = sql;
    }

    /**
     * @return
     * @throws IOException
     */
    public List<TableElement> tables() throws IOException {
        return new MysqlDDLParser().parse(sql);
    }

    public String transferMeta(Map<String, String> mapping) {
        for (Map.Entry<String, String> entry : mapping.entrySet()) {
            metaTemplate = metaTemplate.replace(entry.getKey(), entry.getValue());
        }
        return metaTemplate;
    }

    public String transferFiled(Map<String, String> mapping, boolean primary) {

        String template = fieldTemplate ;
        if (primary){
            template = primaryFieldTemplate ;
        }

        for (Map.Entry<String, String> entry : mapping.entrySet()) {
            template = template.replace(entry.getKey(), entry.getValue());
        }
        return template;
    }

    /**
     *
     * @param s
     * @return
     */
    public String toCamelCase(String s){
        String[] parts = s.split("_");
        String camelCaseString = "";
        for (String part : parts){
            camelCaseString = camelCaseString + toProperCase(part);
        }
        return camelCaseString;
    }

    private String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }


    public String transfer() throws IOException {
        List<TableElement> tables = tables() ;
        StringBuilder pyModel = new StringBuilder() ;

        for (TableElement table : tables) {
            String tableName = table.getTableName().getValue().toString();
            Map<String, String> metaMapping = new HashMap<>();
            metaMapping.put(CLASS_NAME, toCamelCase(tableName));
            metaMapping.put(TABLE_NAME, tableName);
            String metaStr = transferMeta(metaMapping);
            pyModel.append(metaStr) ;

            if(table.getPks().size() == 0){
                throw new RuntimeException("primary key not exits") ;
            }

            for (ColumnElement column : table.getColumns()) {
                String field = column.getColumnName().getValue().toString();
                String fieldType = column.getType().name();
                if (table.getPks().get(0).getValue().equals(field)){
                    //primary key
                    Map<String, String> fieldMapping = new HashMap<>();
                    fieldMapping.put(FILED_NAME, field);
                    fieldMapping.put(FILED_TYPE, "Integer");
                    String primary = transferFiled(fieldMapping, true) ;
                    pyModel.append(primary) ;
                }else {
                    Map<String, String> fieldMapping = new HashMap<>();
                    fieldMapping.put(FILED_NAME, field);

                    if (fieldType.equals("VARCHAR")){
                        String length = getFieldLength(field);
                        fieldMapping.put(FILED_TYPE, DB_TYPE_TO_PY.get(fieldType.toLowerCase()) + "("+length+")");
                    }else {
                        fieldMapping.put(FILED_TYPE, DB_TYPE_TO_PY.get(fieldType.toLowerCase()));
                    }

                    String filed = transferFiled(fieldMapping, false) ;
                    pyModel.append(filed) ;
                }

            }

        }

        return pyModel.toString() ;
    }

    Pattern compile = Pattern.compile("[^0-9]");
    private String getFieldLength(String fileName){
        for (String str : sql.split(",")) {
            if (str.contains(fileName)){
                String result = compile.matcher(str).replaceAll("");
                return result ;
            }
        }

        return "" ;
    }
}
