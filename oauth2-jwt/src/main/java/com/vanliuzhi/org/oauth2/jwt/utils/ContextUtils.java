package com.vanliuzhi.org.oauth2.jwt.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author VanLiuZhi
 * @Date 2020-04-20 11:28
 */
@Component
public class ContextUtils implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // applicationContext.getBean("getCurrentUser");
        System.out.println(applicationContext);
    }
}
