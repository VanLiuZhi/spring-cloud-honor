package com.vanliuzhi.org.common.module.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class ApiIdempotentImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{
                "com.open.capacity.common.config.ApiIdempotentConfig"
        };
    }
}
