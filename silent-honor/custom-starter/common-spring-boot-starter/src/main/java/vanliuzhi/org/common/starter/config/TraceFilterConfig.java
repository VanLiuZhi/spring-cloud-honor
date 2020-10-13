package vanliuzhi.org.common.starter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vanliuzhi.org.common.starter.filter.TraceContextFilter;

/** Trace过滤器注入
 * @author Lys3415
 * @date 2020/10/13 9:23
 */
@Configuration
@ConditionalOnClass(WebMvcConfigurer.class)
public class TraceFilterConfig {
    @Bean
    public FilterRegistrationBean<TraceContextFilter> requestContextRepositoryFilterRegistrationBean() {
        FilterRegistrationBean<TraceContextFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new TraceContextFilter());
        filterFilterRegistrationBean.addUrlPatterns("/*");
        return filterFilterRegistrationBean;
    }
}
