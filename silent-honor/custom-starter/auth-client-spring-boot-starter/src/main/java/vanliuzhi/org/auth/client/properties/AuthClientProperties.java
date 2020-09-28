package vanliuzhi.org.auth.client.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 资源服务器配置属性
 *
 * @author Lys3415
 * @date 2020/9/28 14:10
 */
@Data
@Component
@ConfigurationProperties(prefix = "custom.oauth2.client")
public class AuthClientProperties {

    /**
     * 与签名jwt时使用的密钥,否则解析token失败
     */
    private String signingKey;

    /**
     * 声明资源服务器名称
     * 在oauth_client_details表里的resource_ids字段就是那个客户端可访问的资源服务器列表.
     */
    private String resourceId;

    /**
     * 资源服务器自定义放行端点列表
     */
    private List<String> ignoreUrls;

}
