package top.crossoverjie.plugin.core;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static top.crossoverjie.plugin.core.Status.BASE_FIELD_COMMENT;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020-03-02 22:29
 * @since JDK 1.8
 */
public class StandardDDLLexer {

    private List<TokenResult> results = new ArrayList<>();

    public List<TokenResult> tokenize(String script, Status pid) throws IOException {

        CharArrayReader reader = new CharArrayReader(script.toCharArray());
        DDLTokenType status = DDLTokenType.INIT;
        int ch;
        char value;
        TokenResult result = new TokenResult();
        while ((ch = reader.read()) != -1) {
            value = (char) ch;
            switch (status) {
                case INIT:
                    result = initToken(value, result, pid);
                    status = result.tokenType;
                    break;
                case CT:
                    if (value == '\n') {
                        status = DDLTokenType.INIT;

                        // 继续解析 CREATE TABLE `t` 中的 t
                        Status newStatus = result.status(Status.BASE_CRT);
                        this.tokenize(result.text.toString(), newStatus);

                    } else {
                        result.text.append(value);
                    }
                    break;
                case TBN:
                    if (value == '`') {
                        status = DDLTokenType.INIT;
                    } else {
                        result.text.append(value);
                    }
                    break;
                case FI:
                    if (value == ',') {
                        status = DDLTokenType.INIT;

                        //继续解析 `name` varchar(50) DEFAULT NULL COMMENT '终端机名称' 中的 `name`
                        Status newStatus = result.status(Status.BASE_FIELD_NAME);
                        this.tokenize(result.text.toString(), newStatus);

                        //继续解析 `name` varchar(50) DEFAULT NULL COMMENT '终端机名称' 中的 varchar
                        newStatus = result.status(Status.BASE_FIELD_TYPE);
                        this.tokenize(result.text.toString(), newStatus);

                        //继续解析 `name` varchar(50) DEFAULT NULL COMMENT '终端机名称' 中的 50
                        newStatus = result.status(Status.BASE_FIELD_LEN);
                        this.tokenize(result.text.toString(), newStatus);

                        //继续解析 `name` varchar(50) DEFAULT NULL COMMENT '终端机名称' 中的 '终端机名称'
                        newStatus = result.status(BASE_FIELD_COMMENT);
                        this.tokenize(result.text.toString(), newStatus);


                    } else {
                        result.text.append(value);
                    }
                    break;
                case FIELD_NAME:
                    if (value == '`') {
                        status = DDLTokenType.INIT;
                    } else {
                        result.text.append(value);
                    }
                    break;
                case FIELD_TYPE:

                    // 解析 varchar(50) 为 varchar，所以只能以 ( 结尾
                    if (value == '(') {
                        status = DDLTokenType.INIT;
                    } else {
                        if (isNotFieldType(value)) {
                            status = DDLTokenType.INIT;
                        } else {
                            result.text.append(value);
                        }
                    }
                    break;
                case FIELD_LEN:
                    if (value == ')') {
                        status = DDLTokenType.INIT;
                    } else {
                        result.text.append(value);
                    }
                    break;
                case FIELD_COMMENT:
                    if (value == '\'') {
                        status = DDLTokenType.INIT;
                    } else {
                        result.text.append(value);
                    }
                    break;
                case P_K:
                    if (value == ')') {
                        result.text.append(value);
                        status = DDLTokenType.INIT;

                        // 继续解析 PRIMARY KEY (`id`)--->id
                        Status newStatus = result.status(Status.BASE_FIELD_PK);
                        this.tokenize(result.text.toString(), newStatus);
                    } else {
                        result.text.append(value);
                    }
                    break;
                case P_K_V:
                    if (value == '`') {
                        status = DDLTokenType.INIT;
                    } else {
                        result.text.append(value);
                    }
                    break;

                default:
                    break;

            }
        }
        if (result.getText().length() > 0) {
            results.add(result);
        }


        return results;
    }


    private TokenResult initToken(char value, TokenResult result, Status pid) {

        //再次调用初始化的时候一定是状态转移后，说明可以写入一个完整的数据了。
        if (result.getText().length() > 0) {
            results.add(result);
            result = new TokenResult();
        }

        if (value == 'C' && pid == Status.BASE_INIT) {
            result.tokenType = DDLTokenType.CT;
            result.text.append(value);
        } else if (value == '`' && pid == Status.BASE_INIT) {
            result.tokenType = DDLTokenType.FI;
            result.text.append(value);
        } else if (value == '`' && pid == Status.BASE_CRT) {
            result.tokenType = DDLTokenType.TBN;
        } else if (value == '`' && pid == Status.BASE_FIELD_NAME) {
            result.tokenType = DDLTokenType.FIELD_NAME;
        } else if (value == ' ' && pid == Status.BASE_FIELD_TYPE) {
            result.tokenType = DDLTokenType.FIELD_TYPE;
            result.text.append(value);
        } else if (value == '(' && pid == Status.BASE_FIELD_LEN) {
            result.tokenType = DDLTokenType.FIELD_LEN;
        } else if (value == '\'' && pid == Status.BASE_FIELD_COMMENT) {
            result.tokenType = DDLTokenType.FIELD_COMMENT;
        } else if (value == 'P' && pid == Status.BASE_INIT) {
            result.tokenType = DDLTokenType.P_K;
            result.text.append(value);
        } else if (value == '`' && pid == Status.BASE_FIELD_PK) {
            result.tokenType = DDLTokenType.P_K_V;
        } else {
            result.tokenType = DDLTokenType.INIT;
        }


        return result;

    }


    /**
     * 不属于 fieldType 的字符，NOT NULL AUTO_INCREMENT COMMENT SET utf8mb4 DEFAULT NULL COMMENT
     */
    private String notFieldType = "' N A C S u D";

    private boolean isNotFieldType(char value) {
        for (char c : notFieldType.trim().toCharArray()) {
            if (c == value) {
                return true;
            }
        }
        return false;
    }



    /**
     * whether digit
     *
     * @param value
     * @return
     */
    private boolean isDigit(int value) {
        return value >= 48 && value <= 57;
    }


    public class TokenResult {
        private Status status = Status.BASE_INIT;
        private Text text = new Text();
        private DDLTokenType tokenType;

        public Text getText() {
            return text;
        }

        public Status status() {
            return this.status;
        }

        public Status status(Status status) {
            this.status = status;
            return status;
        }

        public void setText(Text text) {
            this.text = text;
        }

        public DDLTokenType getTokenType() {
            return tokenType;
        }

        public void setTokenType(DDLTokenType tokenType) {
            this.tokenType = tokenType;
        }
    }

    public class Text {
        private StringBuilder text = new StringBuilder();

        /**
         *
         * @param value
         * @param isBlank 是否需要跳过空格
         */
        public void append(char value) {
            text.append(value);
        }



        public int length() {
            return this.text.toString().trim().length();
        }

        @Override
        public String toString() {
            return this.text.toString();
        }
    }


    public class DDLInfo {
        private String tableName;
        private String primaryKey;
        private String comment;

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

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }

    public class FieldInfo {
        private String fieldName;
        private String filedType;
        private String fieldLen;
        private String comment;

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFiledType() {
            return filedType;
        }

        public void setFiledType(String filedType) {
            this.filedType = filedType;
        }

        public String getFieldLen() {
            return fieldLen;
        }

        public void setFieldLen(String fieldLen) {
            this.fieldLen = fieldLen;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }
}
