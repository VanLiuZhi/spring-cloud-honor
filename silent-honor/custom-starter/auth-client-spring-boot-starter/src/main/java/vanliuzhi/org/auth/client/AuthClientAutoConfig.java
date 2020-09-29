package vanliuzhi.org.auth.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import vanliuzhi.org.auth.client.config.ResourceServerConfigurer;
import vanliuzhi.org.auth.client.properties.AuthClientProperties;

/**
 * @author VanLiuZhi
 * @date 2020/9/28 14:00
 */
@EnableResourceServer
@ComponentScan("vanliuzhi.org.auth.client")
@Configuration
@EnableAutoConfiguration
@Import({ResourceServerConfigurer.class})
public class AuthClientAutoConfig {

}
