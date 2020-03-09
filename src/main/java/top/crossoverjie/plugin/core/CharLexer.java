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
public class CharLexer {

    private List<TokenResult> results = new ArrayList<>();

    public List<TokenResult> tokenize(String script) throws IOException {

        CharArrayReader reader = new CharArrayReader(script.toCharArray());
        TokenType status = TokenType.INIT;
        int ch;
        char value;
        TokenResult result = new TokenResult();
        while ((ch = reader.read()) != -1) {
            value = (char) ch;
            switch (status) {
                case INIT:
                    result = initToken(value, result);
                    status = result.tokenType ;
                    break;
                case VAR:
                    if (isDigit(value) || isLetter(value)){
                        result.text.append(value);
                    }else {
                        status = TokenType.INIT;
                    }
                    break;
                case DIGIT:
                    if (isDigit(value)){
                        result.text.append(value);
                    }else {
                        status = TokenType.INIT;
                    }
                    break;
                case GT:
                    if (value == '>'){
                        result.text.append(value);
                    }else {
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


    private TokenResult initToken(char value, TokenResult result) {

        //再次调用初始化的时候一定是状态转移后，说明可以写入一个完整的数据了。
        if (result.getText().length() > 0){
            results.add(result) ;
            result = new TokenResult() ;
        }

        if (isDigit(value)){
            result.tokenType = TokenType.DIGIT;
            result.text.append(value);

        }else if(value == '>'){
            result.tokenType = TokenType.GT;
            result.text.append(value);
        }else if (isLetter(value)){
            result.tokenType = TokenType.VAR;
            result.text.append(value);
        }else {
            result.tokenType = TokenType.INIT;
        }


        return result ;

    }


    /**
     * whether letter
     * @param value
     * @return
     */
    private boolean isLetter(int value) {
        return value >= 65 && value <= 122;
    }

    /**
     * whether digit
     * @param value
     * @return
     */
    private boolean isDigit(int value) {
        return value >=48 && value <= 57;
    }


    public class TokenResult {
        private Text text = new Text();
        private TokenType tokenType ;

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

        public void append(char value){
            text.append(value) ;
        }

        public int length(){
            return this.text.length() ;
        }

        @Override
        public String toString() {
            return this.text.toString() ;
        }
    }
}
