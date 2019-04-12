package cn.farwalker.waka.core;

/**
 * Created by asus on 2019/3/25.
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal();

    public DataSourceContextHolder() {
    }

    public static void setDataSourceType(String dataSourceType) {
        contextHolder.set(dataSourceType);
    }

    public static String getDataSourceType() {
        return (String)contextHolder.get();
    }

    public static void clearDataSourceType() {
        contextHolder.remove();
    }
}
