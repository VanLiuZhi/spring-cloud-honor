package com.vanliuzhi.org.custom.log.annotation;

import com.vanliuzhi.org.custom.log.selector.LogImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Description 启动日志框架支持
 * 自动装配starter，需要配置多数据源
 * @Author VanLiuZhi
 * @Date 2020-04-19 23:48
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LogImportSelector.class)
public @interface EnableLogging {
    // String name();
}
