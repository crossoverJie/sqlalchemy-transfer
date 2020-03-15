package top.crossoverjie.plugin.core;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020-03-07 23:19
 * @since JDK 1.8
 */
public enum DDLTokenType {
    INIT,
    CT, //create info
    FI,//field info,
    TBN,//table name
    FIELD_NAME,
    FIELD_TYPE,
    FIELD_LEN,
    FIELD_COMMENT,
    P_K,
    P_K_V, //主键值
    VAR,
    DIGIT,
    NONE

}
