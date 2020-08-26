package com.vanliuzhi.org.custom.datasource.util;

import com.vanliuzhi.org.custom.datasource.constant.DataSourceKey;

/**
 * @Description 用于数据源切换
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
public class DataSourceHolder {

    /**
     * 注意使用ThreadLocal，微服务下游建议使用信号量
     */
    private static final ThreadLocal<DataSourceKey> DATA_SOURCE_KEY = new ThreadLocal<>();

    /**
     * 得到当前的数据库连接
     */
    public static DataSourceKey getDataSourceKey() {
        return DATA_SOURCE_KEY.get();
    }

    /**
     * 设置当前的数据库连接
     */
    public static void setDataSourceKey(DataSourceKey type) {
        DATA_SOURCE_KEY.set(type);
    }

    /**
     * 清除当前的数据库连接
     */
    public static void clearDataSourceKey() {
        DATA_SOURCE_KEY.remove();
    }

}
