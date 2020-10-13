package vanliuzhi.org.common.starter.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vanliuzhi.org.common.starter.constant.TraceConstant;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Trace 过滤器实现
 *
 * @author Lys3415
 * @date 2020/10/13 9:34
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 8)
@ConditionalOnClass(WebMvcConfigurer.class)
public class TraceContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        //请求头传入存在以请求头传入的为准，不然以X-B3-TraceId为
        String appTraceId = StringUtils.defaultString(
                httpServletRequest.getHeader(TraceConstant.HTTP_HEADER_TRACE_ID),
                MDC.get(TraceConstant.LOG_B3_TRACE_ID));
        //未经过HandlerInterceptor的设置，但是有请求头，重新设置
        if (StringUtils.isNotEmpty(appTraceId)) {
            MDC.put(TraceConstant.LOG_TRACE_ID, appTraceId);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
