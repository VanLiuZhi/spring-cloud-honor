package com.vanliuzhi.org.custom.datasource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.vanliuzhi.org.custom.datasource.aop.DataSourceAop;
import com.vanliuzhi.org.custom.datasource.constant.DataSourceKey;
import com.vanliuzhi.org.custom.datasource.util.DynamicDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @Description 多数据自动装配
 * @Author VanLiuZhi
 * @Date 2020-04-22 14:13
 */
@Configuration
@Import(DataSourceAop.class)
@AutoConfigureBefore(value = {DruidDataSourceAutoConfigure.class, MybatisPlusAutoConfiguration.class})
@ConditionalOnProperty(name = {"spring.datasource.dynamic.enable"}, matchIfMissing = false, havingValue = "true")
public class CustomDataSourceAutoConfig {

    /**
     * 创建数据源，所有引入db-core的模块都需要一个核心库，可以是user-center，也可以是oauth-center, file-center, sms-center
     */
    @Bean
    @ConfigurationProperties("spring.datasource.druid.core")
    public DataSource dataSourceCore() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 所有的核心库共享一个日志中心模块，该模块不采用mysql中的innodb引擎，采用归档引擎
     */
    @Bean
    @ConfigurationProperties("spring.datasource.druid.log")
    public DataSource dataSourceLog() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 只需要纳入动态数据源到spring容器
     */
    @Primary
    @Bean
    public DataSource dataSource() {
        DynamicDataSource dataSource = new DynamicDataSource();
        DataSource coreDataSource = dataSourceCore();
        DataSource logDataSource = dataSourceLog();
        dataSource.addDataSource(DataSourceKey.core, coreDataSource);
        dataSource.addDataSource(DataSourceKey.log, logDataSource);
        dataSource.setDefaultTargetDataSource(coreDataSource);
        return dataSource;
    }

    /**
     * 将数据源纳入spring事物管理
     */
    @Bean
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
