package vanliuzhi.org.common.starter.auth.props;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @author lys3415
 */
@ConfigurationProperties(prefix = "security.oauth2")
public class PermitUrlProperties {

    /**
     * 监控中心和swagger需要访问的url
     */
    private static final String[] ENDPOINTS = {
            //端点监控
            "/**/actuator/**", "/**/actuator/**/**",
            //端点监控
            "/**/doc.html", "/doc.html",
            // api-gateway webflux swagger
            "/v2/api-docs/**", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**",
            //业务中心swagger
            "/**/v2/api-docs/**", "/**/swagger-ui.html", "/**/swagger-resources/**", "/**/webjars/**",
            //熔断监控
            "/**/turbine.stream", "/**/turbine.stream**/**", "/**/hystrix", "/**/hystrix.stream", "/**/hystrix/**",
            "/**/hystrix/**/**", "/**/proxy.stream/**",
            "/**/druid/**", "/**/favicon.ico", "/**/prometheus"
    };

    private String[] ignored;

    /**
     * 需要放开权限的url
     *
     * @return 自定义的url和监控中心需要访问的url集合
     */
    public String[] getIgnored() {
        if (ignored == null || ignored.length == 0) {
            return ENDPOINTS;
        }
        return ArrayUtils.addAll(ignored, ENDPOINTS);
    }

    public void setIgnored(String[] ignored) {
        this.ignored = ignored;
    }

}
