package top.crossoverjie.plugin.core;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020-03-02 22:29
 * @since JDK 1.8
 */
public class SqlLexer {

    private List<TokenResult> results = new ArrayList<>();

    public List<TokenResult> tokenize(String script, int depth) throws IOException {

        CharArrayReader reader = new CharArrayReader(script.toCharArray());
        TokenType status = TokenType.INIT;
        int ch;
        char value;
        TokenResult result = new TokenResult();
        while ((ch = reader.read()) != -1) {
            value = (char) ch;
            switch (status) {
                case INIT:
                    result = initToken(value, result, depth);
                    status = result.tokenType;
                    break;

                case FIELD:
                    if (value == '`') {
                        status = TokenType.INIT;

                        // 将结尾的字符串 ` 写入
                        result.text.append(value);
                    } else if (isLetter(value)) {
                        result.text.append(value);
                    }
                    break;

                case FIELD_TYPE:
                    // int  decimal datetime varchar
                    if (value == 't' || value == 'l' || value == 'e' || value == 'r' && !result.text.isVarchar()) {

                        status = TokenType.INIT;

                        //结尾字母写入
                        result.text.append(value);
                    } else {
                        result.text.append(value);
                    }
                    break;
                case FIELD_LEN:
                    if (value == ')') {
                        status = TokenType.INIT;

                        //结尾字母写入
                        result.text.append(value);
                    } else {
                        result.text.append(value);

                        if (value == '\n') {
                            // 换行符的字符单独额外出来
                            result = new TokenResult();
                            status = TokenType.INIT;
                        }
                    }
                    break;
                case COMMENT:
                    if (value == '\'') {
                        status = TokenType.INIT;

                        //结尾字母写入
                        result.text.append(value);
                    } else {
                        result.text.append(value);
                    }
                    break;
                case P_K:

                    if (value == ')') {
                        status = TokenType.INIT;

                        //结尾字母写入
                        result.text.append(value);

                        //递归找出真正的主键
                        int pid = result.nextPid() ;
                        if (pid == 1){
                            this.tokenize(result.text.toString(), pid) ;
                        }
                    } else {
                        result.text.append(value);
                    }
                    break;
                case P_K_V:

                    if (value == '`') {
                        status = TokenType.INIT;
                    } else {
                        result.text.append(value);
                    }
                    break;
                case COMMAND:
                    if (isLetter(value)) {
                        result.text.append(value);
                    } else {
                        status = TokenType.INIT;
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



    private TokenResult initToken(char value, TokenResult result, int depth) {

        //再次调用初始化的时候一定是状态转移后，说明可以写入一个完整的数据了。
        if (result.getText().length() > 0) {
            results.add(result);
            result = new TokenResult();
        }

        if (value == 'P' && depth == 0) {
            result.tokenType = TokenType.P_K;
            result.text.append(value);
        } else if (isPrimaryKey(value) && depth != 0) {
            // 匹配出主键 ID
            result.tokenType = TokenType.P_K_V;
            result.text.append(value);
        } else if (value == '`' && depth == 0) {
            result.tokenType = TokenType.FIELD;
            result.text.append(value);
        } else if ((value == 'i' || value == 'd' || value == 'v') && depth == 0) {
            result.tokenType = TokenType.FIELD_TYPE;
            result.text.append(value);
        } else if (value == '(' && depth == 0) {
            result.tokenType = TokenType.FIELD_LEN;
            result.text.append(value);
        } else if (value == '\'') {
            result.tokenType = TokenType.COMMENT;
            result.text.append(value);
        } else if (isLetter(value) && depth == 0) {
            result.tokenType = TokenType.COMMAND;
            result.text.append(value);
        } else {
            result.tokenType = TokenType.INIT;
        }


        return result;

    }


    /**
     * 是否字母
     *
     * @param value
     * @return
     */
    private boolean isLetter(int value) {
        return value >= 65 && value <= 122;
    }

    private boolean isPrimaryKey(char value){
        if (!isLetter(value)){
            return false ;
        }

        String primaryKey = "`PRIMARY KEY (" ;
        for (char c : primaryKey.toCharArray()) {
            if (c == value){
                return false ;
            }
        }

        return true ;
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
        private Integer pid = 0;
        private Text text = new Text();
        private TokenType tokenType;

        public int nextPid(){
            return ++pid ;
        }
        public int pid(){
            return pid ;
        }

        public Text getText() {
            return text;
        }

        public void setText(Text text) {
            this.text = text;
        }

        public TokenType getTokenType() {
            return tokenType;
        }

        public void setTokenType(TokenType tokenType) {
            this.tokenType = tokenType;
        }
    }

    public class Text {
        private StringBuilder text = new StringBuilder();

        public void append(char value) {
            text.append(value);
        }

        /**
         * 兼容 varchar
         *
         * @return
         */
        public boolean isVarchar() {
            if (this.text.toString().equals("va")) {
                return true;
            } else {
                return false;
            }
        }

        public int length() {
            return this.text.length();
        }

        @Override
        public String toString() {
            return this.text.toString();
        }
    }
}
