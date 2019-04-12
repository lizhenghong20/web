package cn.farwalker.waka.core;

/**
 * Created by asus on 2019/3/25.
 */
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
    public DynamicDataSource() {
    }

    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSourceType();
    }
}