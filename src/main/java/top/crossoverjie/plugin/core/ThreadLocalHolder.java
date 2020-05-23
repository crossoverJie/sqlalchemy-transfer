package top.crossoverjie.plugin.core;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020-05-23 22:32
 * @since JDK 1.8
 */
public class ThreadLocalHolder {

    private static final ThreadLocal<Boolean> DECIMAL = new ThreadLocal<>();

    public static void setDecimal(boolean checked) {
        DECIMAL.set(checked);
    }

    public static boolean getDecimal() {
        return DECIMAL.get() == null ? false : DECIMAL.get();
    }

    public static void removeDecimal() {
        DECIMAL.remove();
    }
}
