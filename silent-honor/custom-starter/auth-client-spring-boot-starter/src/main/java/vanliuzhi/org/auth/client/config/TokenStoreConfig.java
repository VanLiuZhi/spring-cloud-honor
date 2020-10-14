package vanliuzhi.org.auth.client.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.util.Assert;
import vanliuzhi.org.auth.client.token.RedisTemplateTokenStore;

/**
 * @author Lys3415
 * @date 2020/10/13 16:25
 */
@Configuration
public class TokenStoreConfig {

    @Bean
    @ConditionalOnProperty(prefix = "security.oauth2.token.store", name = "type", havingValue = "redis", matchIfMissing = true)
    public RedisTemplateTokenStore redisTemplateTokenStore(RedisConnectionFactory redisConnectionFactory) {
        Assert.state(redisConnectionFactory != null, "connectionFactory must be provided");
        return new RedisTemplateTokenStore(redisConnectionFactory);
    }

}
