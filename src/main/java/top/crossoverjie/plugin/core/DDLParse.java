package top.crossoverjie.plugin.core;

import org.apache.commons.lang3.StringUtils;
import top.crossoverjie.plugin.core.parse.DDLInfo;
import top.crossoverjie.plugin.core.parse.DDLTokenType;
import top.crossoverjie.plugin.core.parse.FieldInfo;
import top.crossoverjie.plugin.core.parse.StandardDDLLexer;
import top.crossoverjie.plugin.core.parse.Status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static top.crossoverjie.plugin.core.Constants.CLASS_NAME;
import static top.crossoverjie.plugin.core.Constants.DB_TYPE_TO_PY;
import static top.crossoverjie.plugin.core.Constants.FILED_COMMENT;
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

    private String fieldTemplate = "    " + FILED_NAME + " = db.Column(db." + FILED_TYPE + ")  # " + FILED_COMMENT + "\n";

    private String sql;

    public DDLParse(String sql) {
        this.sql = compatibleSymbol(sql).toString();
    }

    /**
     * compatible "Drop table"
     *
     * @param str
     * @return
     */
    private StringBuilder compatibleSymbol(String str) {
        StringBuilder sb = new StringBuilder();
        for (String value : str.split(";")) {
            if (!value.trim().startsWith("CREATE TABLE")) {
                continue;
            }
            sb.append(value).append(";");
        }
        return sb;
    }

    /**
     * @return
     * @throws IOException
     */
    public List<DDLInfo> generateDDLInfo() throws IOException {
        List<DDLInfo> ddlInfoList = new ArrayList<>();
        for (String script : sql.split(";")) {

            DDLInfo ddlInfo = new DDLInfo();
            StandardDDLLexer lexer = new StandardDDLLexer();
            List<StandardDDLLexer.TokenResult> tokenize = lexer.tokenize(script, Status.BASE_INIT, 0);

            List<Integer> fi = new ArrayList<>();
            Map<Integer, List<StandardDDLLexer.TokenResult>> childInfoMapping = new HashMap<>(16);
            List<StandardDDLLexer.TokenResult> tokenResultList = new ArrayList<>();
            for (StandardDDLLexer.TokenResult result : tokenize) {
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

            ddlInfo.setFieldInfos(fieldInfos);
            ddlInfoList.add(ddlInfo);
        }


        return ddlInfoList;

    }

    public String transferMeta(Map<String, String> mapping) {
        for (Map.Entry<String, String> entry : mapping.entrySet()) {
            metaTemplate = metaTemplate.replace(entry.getKey(), entry.getValue());
        }
        return metaTemplate;
    }

    public String transferFiled(Map<String, String> mapping, boolean primary) {

        String template = fieldTemplate;
        if (primary) {
            template = primaryFieldTemplate;
        }

        for (Map.Entry<String, String> entry : mapping.entrySet()) {
            template = template.replace(entry.getKey(), entry.getValue());
        }
        return template;
    }

    /**
     * @param s
     * @return
     */
    public String toCamelCase(String s) {
        String[] parts = s.split("_");
        String camelCaseString = "";
        for (String part : parts) {
            camelCaseString = camelCaseString + toProperCase(part);
        }
        return camelCaseString;
    }

    private String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }


    public String transfer() throws IOException {
        List<DDLInfo> tables = generateDDLInfo();
        StringBuilder pyModel = new StringBuilder();

        for (DDLInfo table : tables) {
            String tableName = table.getTableName();
            Map<String, String> metaMapping = new HashMap<>();
            metaMapping.put(CLASS_NAME, toCamelCase(tableName));
            metaMapping.put(TABLE_NAME, tableName);
            String metaStr = transferMeta(metaMapping);
            pyModel.append(metaStr);

            if (StringUtils.isEmpty(table.getPrimaryKey())) {
                throw new RuntimeException("primary key not exits");
            }

            for (FieldInfo fieldInfo : table.getFieldInfos()) {
                String fieldName = fieldInfo.getFieldName();
                String fieldType = fieldInfo.getFiledType();

                if (table.getPrimaryKey().equals(fieldName)) {
                    //primary key
                    Map<String, String> fieldMapping = new HashMap<>();
                    fieldMapping.put(FILED_NAME, fieldName);
                    fieldMapping.put(FILED_TYPE, fieldType);
                    String primary = transferFiled(fieldMapping, true);
                    pyModel.append(primary);
                } else {
                    Map<String, String> fieldMapping = new HashMap<>();
                    fieldMapping.put(FILED_NAME, fieldName);

                    fieldMapping.put(FILED_TYPE, DB_TYPE_TO_PY.get(fieldType.toLowerCase()));
                    fieldMapping.put(FILED_COMMENT, fieldInfo.getComment());

                    String filed = transferFiled(fieldMapping, false);
                    pyModel.append(filed);

                }

            }

        }

        return pyModel.toString();
    }

    Pattern compile = Pattern.compile("[^0-9]");

    private String getFieldLength(String fileName) {
        for (String str : sql.split(",")) {
            if (str.contains(fileName)) {
                String result = compile.matcher(str).replaceAll("");
                return result;
            }
        }

        return "";
    }
}
