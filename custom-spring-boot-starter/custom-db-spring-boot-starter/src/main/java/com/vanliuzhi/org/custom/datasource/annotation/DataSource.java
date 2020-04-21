package com.vanliuzhi.org.custom.datasource.annotation;

import java.lang.annotation.*;

/**
 * @Description 数据源选择
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    //数据库名称
    String name();
}
