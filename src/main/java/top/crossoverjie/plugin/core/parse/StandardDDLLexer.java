package top.crossoverjie.plugin.core.parse;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static top.crossoverjie.plugin.core.parse.Status.BASE_FIELD_COMMENT;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020-03-02 22:29
 * @since JDK 1.8
 */
public class StandardDDLLexer {

    private List<TokenResult> results = new ArrayList<>();

    private int id = 0 ;

    public List<TokenResult> tokenize(String script, Status pStatus, int pid) throws IOException {

        CharArrayReader reader = new CharArrayReader(script.toCharArray());
        DDLTokenType status = DDLTokenType.INIT;
        int ch;
        char value;
        TokenResult result = new TokenResult();
        while ((ch = reader.read()) != -1) {
            value = (char) ch;
            switch (status) {
                case INIT:
                    result = initToken(value, result, pStatus);
                    status = result.tokenType;
                    break;
                case CT:
                    if (value == '\n') {
                        status = DDLTokenType.INIT;

                        // 继续解析 CREATE TABLE `t` 中的 t
                        Status newStatus = result.status(Status.BASE_CRT);
                        this.tokenize(result.text.toString(), newStatus, result.pid);

                    } else {
                        result.text.append(value);
                    }
                    break;
                case TBN:
                    if (value == '`') {
                        status = DDLTokenType.INIT;
                        result.pid = pid ;
                    } else {
                        result.text.append(value);
                    }
                    break;
                case FI:
                    if (value == ',') {
                        status = DDLTokenType.INIT;
                        result.pid = nextPid() ;

                        //继续解析 `name` varchar(50) DEFAULT NULL COMMENT '终端机名称' 中的 `name`
                        Status newStatus = result.status(Status.BASE_FIELD_NAME);
                        this.tokenize(result.text.toString(), newStatus,result.pid);

                        //继续解析 `name` varchar(50) DEFAULT NULL COMMENT '终端机名称' 中的 varchar
                        newStatus = result.status(Status.BASE_FIELD_TYPE);
                        this.tokenize(result.text.toString(), newStatus,result.pid);

                        //继续解析 `name` varchar(50) DEFAULT NULL COMMENT '终端机名称' 中的 50
                        newStatus = result.status(Status.BASE_FIELD_LEN);
                        this.tokenize(result.text.toString(), newStatus,result.pid);

                        //继续解析 `name` varchar(50) DEFAULT NULL COMMENT '终端机名称' 中的 '终端机名称'
                        newStatus = result.status(BASE_FIELD_COMMENT);
                        this.tokenize(result.text.toString(), newStatus,result.pid);


                    } else {
                        result.text.append(value);
                    }
                    break;
                case FIELD_NAME:
                    if (value == '`') {
                        status = DDLTokenType.INIT;
                        result.pid = pid ;
                    } else {
                        result.text.append(value);
                    }
                    break;
                case FIELD_TYPE:

                    // 解析 varchar(50) 为 varchar，所以只能以 ( 结尾
                    if (value == '(') {
                        status = DDLTokenType.INIT;
                        result.pid = pid ;
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
                        result.pid = pid ;
                    } else {
                        result.text.append(value);
                    }
                    break;
                case FIELD_COMMENT:
                    if (value == '\'') {
                        status = DDLTokenType.INIT;
                        result.pid = pid ;
                    } else {
                        result.text.append(value);
                    }
                    break;
                case P_K:
                    if (value == ')') {
                        result.text.append(value);
                        status = DDLTokenType.INIT;
                        result.pid = nextPid() ;

                        // 继续解析 PRIMARY KEY (`id`)--->id
                        Status newStatus = result.status(Status.BASE_FIELD_PK);
                        this.tokenize(result.text.toString(), newStatus, result.pid);
                    } else {
                        result.text.append(value);
                    }
                    break;
                case P_K_V:
                    if (value == '`') {
                        status = DDLTokenType.INIT;
                        result.pid = pid ;
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

    /**
     *
     * @param value
     * @param result
     * @param pStatus 用于递归校验状态
     * @return
     */
    private TokenResult initToken(char value, TokenResult result, Status pStatus) {

        //再次调用初始化的时候一定是状态转移后，说明可以写入一个完整的数据了。
        if (result.getText().length() > 0) {
            results.add(result);
            result = new TokenResult();
        }

        if (value == 'C' && pStatus == Status.BASE_INIT) {
            result.tokenType = DDLTokenType.CT;
            result.text.append(value);
        } else if (value == '`' && pStatus == Status.BASE_INIT) {
            result.tokenType = DDLTokenType.FI;
            result.text.append(value);
        } else if (value == '`' && pStatus == Status.BASE_CRT) {
            result.tokenType = DDLTokenType.TBN;
        } else if (value == '`' && pStatus == Status.BASE_FIELD_NAME) {
            result.tokenType = DDLTokenType.FIELD_NAME;
        } else if (value == ' ' && pStatus == Status.BASE_FIELD_TYPE) {
            result.tokenType = DDLTokenType.FIELD_TYPE;
            result.text.append(value);
        } else if (value == '(' && pStatus == Status.BASE_FIELD_LEN) {
            result.tokenType = DDLTokenType.FIELD_LEN;
        } else if (value == '\'' && pStatus == Status.BASE_FIELD_COMMENT) {
            result.tokenType = DDLTokenType.FIELD_COMMENT;
        } else if (value == 'P' && pStatus == Status.BASE_INIT) {
            result.tokenType = DDLTokenType.P_K;
            result.text.append(value);
        } else if (value == '`' && pStatus == Status.BASE_FIELD_PK) {
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


    private int nextPid(){
        return ++this.id ;
    }


    public class TokenResult {
        private Status status = Status.BASE_INIT;
        private Text text = new Text();
        private DDLTokenType tokenType;
        private int pid = 0;

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

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
    
}
