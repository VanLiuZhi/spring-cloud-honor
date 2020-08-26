package com.vanliuzhi.org.custom.datasource.util;

import com.vanliuzhi.org.custom.datasource.constant.DataSourceKey;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description spring动态数据源（需要继承AbstractRoutingDataSource）
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private final Map<Object, Object> dataSources;

    public DynamicDataSource() {
        dataSources = new HashMap<>();

        super.setTargetDataSources(dataSources);

    }

    public <T extends DataSource> void addDataSource(DataSourceKey key, T data) {
        dataSources.put(key, data);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceHolder.getDataSourceKey();
    }

}
