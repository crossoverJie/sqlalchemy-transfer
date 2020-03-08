package top.crossoverjie.plugin.core.lab;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020-03-05 02:03
 * @since JDK 1.8
 */
public interface Token {
    /**
     * Token的类型
     * @return
     */
    public TokenType getType();

    /**
     * Token的文本值
     * @return
     */
    public String getText();
}
