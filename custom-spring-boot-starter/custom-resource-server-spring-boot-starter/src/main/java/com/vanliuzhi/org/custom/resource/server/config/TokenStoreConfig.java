package com.vanliuzhi.org.custom.resource.server.config;

import com.vanliuzhi.org.custom.resource.server.token.RedisTemplateTokenStore;
import com.vanliuzhi.org.custom.resource.server.token.ResJwtAccessTokenConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @Description token 存储配置，支持jdb，Redis，jwt的方式
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
@Configuration
public class TokenStoreConfig {

    @Resource
    private DataSource dataSource;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 使用数据库存储的时候，装配
     */
    @Bean
    @ConditionalOnProperty(prefix = "security.oauth2.token.store", name = "type", havingValue = "jdbc", matchIfMissing = false)
    public JdbcTokenStore jdbcTokenStore() {
        // oauth_access_token oauth_refresh_token 创建两张表
        // return new JdbcTokenStore( dataSource ) ;
        return new JdbcTokenStore(dataSource);
    }

    /**
     * 使用Redis存储的时候，装配
     */
    @Bean
    @ConditionalOnProperty(prefix = "security.oauth2.token.store", name = "type", havingValue = "redis", matchIfMissing = true)
    public RedisTemplateTokenStore redisTokenStore() {
        // return new RedisTokenStore( redisTemplate.getConnectionFactory() ); //单台redis服务器
        RedisTemplateTokenStore redisTemplateStore = new RedisTemplateTokenStore();
        redisTemplateStore.setRedisTemplate(redisTemplate);
        return redisTemplateStore;
    }

    // 使用jwt替换原有的uuid生成token方式
    @Configuration
    @ConditionalOnProperty(prefix = "security.oauth2.token.store", name = "type", havingValue = "jwt", matchIfMissing = false)
    public static class JWTTokenConfig {
        // 定义 JwtTokenStore 此时已经是jwt
        @Bean
        public JwtTokenStore jwtTokenStore() {
            return new JwtTokenStore(jwtAccessTokenConverter());
        }

        // 定义转换
        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter() {
            JwtAccessTokenConverter accessTokenConverter = new ResJwtAccessTokenConverter();
            accessTokenConverter.setSigningKey("ocp"); // 配置JWT使用的秘钥
            return accessTokenConverter;
        }

        // 扩展: TokenEnhancer 实现该接口的类能提供jwt增强，可以自定义一些数据
    }

}
