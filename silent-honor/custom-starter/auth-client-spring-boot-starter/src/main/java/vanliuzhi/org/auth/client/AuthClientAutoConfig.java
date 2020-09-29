package vanliuzhi.org.auth.client;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import vanliuzhi.org.auth.client.config.ResourceServerConfigurer;

/**
 * @author VanLiuZhi
 * @date 2020/9/28 14:00
 */
@EnableResourceServer
@ComponentScan("vanliuzhi.org.auth.client")
@Configuration
@Import({ResourceServerConfigurer.class})
public class AuthClientAutoConfig {

}
