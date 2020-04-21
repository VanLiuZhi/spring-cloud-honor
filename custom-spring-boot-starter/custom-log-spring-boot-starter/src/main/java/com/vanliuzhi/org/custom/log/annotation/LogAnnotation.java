package com.vanliuzhi.org.custom.log.annotation;

import java.lang.annotation.*;

/**
 * @Description 日志注解
 * recordRequestParam: true需要配置log数据源
 * @Author VanLiuZhi
 * @Date 2020-04-19 23:48
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    /**
     * 模块
     *
     * @return
     */
    String module();

    /**
     * 记录执行参数
     *
     * @return
     */
    boolean recordRequestParam() default false;
}
