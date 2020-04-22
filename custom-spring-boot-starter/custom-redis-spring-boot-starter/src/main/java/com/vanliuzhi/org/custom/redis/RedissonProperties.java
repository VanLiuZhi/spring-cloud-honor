package com.vanliuzhi.org.custom.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description RedissonProperties
 * @Author VanLiuZhi
 * @Date 2020-04-19 23:48
 */
@ConfigurationProperties(prefix = "spring.redis.redisson")
public class RedissonProperties {

    private String config;
    private String enable;

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

}
