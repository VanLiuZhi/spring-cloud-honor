package com.vanliuzhi.org.custom.log.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Description log-spring-boot-starter 自动装配
 * @Author VanLiuZhi
 * @Date 2020-04-19 23:48
 */
public class LogImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        // TODO Auto-generated method stub
        // importingClassMetadata.getAllAnnotationAttributes(EnableEcho.class.getName());

        return new String[]{
                "com.open.capacity.log.aop.LogAnnotationAOP",
                "com.open.capacity.log.service.impl.LogServiceImpl",
                "com.open.capacity.log.config.LogAutoConfig"

        };
    }

}
