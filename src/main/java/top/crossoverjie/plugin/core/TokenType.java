package top.crossoverjie.plugin.core;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020-03-07 23:19
 * @since JDK 1.8
 */
public enum TokenType {
    INIT,
    COMMAND,
    CT, //创建 table
    TABLE_NAME,
    FIELD,
    FIELD_TYPE,
    FIELD_LEN,
    P_K,
    P_K_V, //主键值
    COMMENT,
    VAR,
    GT,
    DIGIT,
    NONE
}
