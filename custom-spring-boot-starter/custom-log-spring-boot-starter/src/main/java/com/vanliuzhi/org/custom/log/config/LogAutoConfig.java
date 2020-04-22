// package com.vanliuzhi.org.custom.log.config;
//
// import com.vanliuzhi.org.custom.log.interceptor.LogInterceptor;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
// import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
// /**
//  * @Description 日志拦截器，排除对spring cloud gateway的影响 (WebMvcConfigurer)
//  * @Author VanLiuZhi
//  * @Date 2020-04-19 23:48
//  */
// @ConditionalOnClass(WebMvcConfigurer.class)
// public class LogAutoConfig implements WebMvcConfigurer {
//
//     @Override
//     public void addInterceptors(InterceptorRegistry registry) {
//
//         /**
//          * 自定义拦截器，添加拦截路径和排除拦截路径
//          * addPathPatterns():添加需要拦截的路径
//          * excludePathPatterns():添加不需要拦截的路径
//          * 在括号中还可以使用集合的形式，如注释部分代码所示
//          */
//         registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**");
//
//     }
// }
