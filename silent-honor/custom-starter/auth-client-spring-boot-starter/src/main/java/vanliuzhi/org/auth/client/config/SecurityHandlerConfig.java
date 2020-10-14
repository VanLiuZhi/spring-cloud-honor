package vanliuzhi.org.auth.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lys3415
 * @date 2020/10/13 11:23
 */
@Configuration
public class SecurityHandlerConfig {

    /**
     * 注入json处理类，spring mvc启动的时候自动注入
     */
    @Resource
    private ObjectMapper objectMapper;

    /**
     * 登录失败
     */
    @Bean
    public AuthenticationFailureHandler loginFailureHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse,
                                                AuthenticationException e) throws IOException, ServletException {
                String msg = e.getMessage();
                if (e instanceof BadCredentialsException) {
                    msg = "BadCredentialsException 密码错误";
                }
                Map<String, String> rsp = new HashMap<>(2);
                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());

                rsp.put("code", String.valueOf(HttpStatus.UNAUTHORIZED.value()));
                rsp.put("msg", msg);

                httpServletResponse.setContentType("application/json; charset=UTF-8");
                httpServletResponse.getWriter().write(objectMapper.writeValueAsString(rsp));
                httpServletResponse.getWriter().flush();
                httpServletResponse.getWriter().close();
            }
        };
    }

    /**
     * 未登录
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                 AuthenticationException e) throws IOException, ServletException {
                Map<String, String> rsp = new HashMap<>(2);
                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());

                rsp.put("code", String.valueOf(HttpStatus.UNAUTHORIZED.value()));
                rsp.put("msg", e.getMessage());

                httpServletResponse.setContentType("application/json; charset=UTF-8");
                httpServletResponse.getWriter().write(objectMapper.writeValueAsString(rsp));
                httpServletResponse.getWriter().flush();
                httpServletResponse.getWriter().close();
            }
        };
    }

    @Bean
    public OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler() {
        return new OAuth2AccessDeniedHandler() {

            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response,
                               AccessDeniedException authException) throws IOException, ServletException {
                Map<String, String> data = new HashMap<>(2);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());

                data.put("code", String.valueOf(HttpStatus.UNAUTHORIZED.value()));
                data.put("msg", authException.getMessage());

                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(data));
                response.getWriter().flush();
                response.getWriter().close();
            }
        };
    }

    @Bean
    public OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler(ApplicationContext applicationContext) {
        OAuth2WebSecurityExpressionHandler expressionHandler = new OAuth2WebSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        return expressionHandler;
    }

}
