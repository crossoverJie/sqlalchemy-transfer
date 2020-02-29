package top.crossoverjie.plugin.core;

import com.moilioncircle.ddl.parser.MysqlDDLParser;
import com.moilioncircle.ddl.parser.TableElement;

import java.io.IOException;
import java.util.List;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020-02-29 23:29
 * @since JDK 1.8
 */
public class DDLParse {

    /**
     *
     * @param sql
     * @return
     * @throws IOException
     */
    public static List<TableElement> tables(String sql) throws IOException {
        return new MysqlDDLParser().parse(sql);
    }
}
