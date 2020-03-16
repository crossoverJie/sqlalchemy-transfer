package top.crossoverjie.plugin.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020-03-01 02:24
 * @since JDK 1.8
 */
public class Constants {

    public static final String CLASS_NAME = "{class_name}" ;
    public static final String TABLE_NAME = "{table_name}" ;
    public static final String FILED_NAME = "{field_name}" ;
    public static final String FILED_TYPE = "{filed_type}" ;
    public static final String FILED_COMMENT = "{filed_comment}" ;



    public static final Map<String,String> DB_TYPE_TO_PY = new HashMap<>() ;
    static {
        DB_TYPE_TO_PY.put("varchar", "String") ;
        DB_TYPE_TO_PY.put("int", "Integer") ;
        DB_TYPE_TO_PY.put("datetime", "DateTime") ;
        DB_TYPE_TO_PY.put("decimal", "Float") ;
    }
}
