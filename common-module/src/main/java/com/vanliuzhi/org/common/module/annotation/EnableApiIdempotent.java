package com.vanliuzhi.org.common.module.annotation;

import com.vanliuzhi.org.common.module.selector.ApiIdempotentImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启动幂等拦截器
 * 自动装配starter
 * 选择器
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@Import(ApiIdempotentImportSelector.class)
public @interface EnableApiIdempotent {
}
