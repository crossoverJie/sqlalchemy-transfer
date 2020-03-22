package top.crossoverjie.plugin.core.lab;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020-03-22 21:45
 * @since JDK 1.8
 */
public class TestLexer {



    List<Result> results = new ArrayList<>() ;

    public List<Result> tokenize(String script) throws IOException {
        CharArrayReader reader = new CharArrayReader(script.toCharArray());
        TokenType status = TokenType.INIT;
        int ch;
        char value;
        Result result = new Result();
        while ((ch = reader.read()) != -1) {
            value = (char) ch;
            switch (status) {
                case INIT:
                    result = initToken(value, result);
                    status = result.tokenType;
                    break;
                case VAR:
                    if (isLetter(value)){
                        result.text.append(value);
                    }else {
                        status = TokenType.INIT;
                    }
                    break;
                case GE:
                    if (value == '='){
                        result.text.append(value);
                    }else {
                        status = TokenType.INIT;
                    }
                    break;
                case VAL:
                    if (isDigit(value)){
                        result.text.append(value);
                    }else {
                        status = TokenType.INIT;
                    }
                break;
            }
        }

        if (result.text.length() > 0) {
            results.add(result);
        }

        return results;
    }


    private Result initToken(char value, Result result){
        //再次调用初始化的时候一定是状态转移后，说明可以写入一个完整的数据了。
        if (result.text.length() > 0) {
            results.add(result);
            result = new Result();
        }

        if (isLetter(value)){
            result.tokenType = TokenType.VAR ;
            result.text.append(value);
        }else if (value == '='){
            result.tokenType = TokenType.GE ;
            result.text.append(value);
        }else if (isDigit(value)){
            result.tokenType = TokenType.VAL ;
            result.text.append(value);
        }else {
            result.tokenType = TokenType.INIT ;
        }

        return result ;

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

    /**
     * whether digit
     *
     * @param value
     * @return
     */
    private boolean isDigit(int value) {
        return value >= 48 && value <= 57;
    }

    public class Result{
        public TokenType tokenType ;
        public StringBuilder text = new StringBuilder();
    }
}
