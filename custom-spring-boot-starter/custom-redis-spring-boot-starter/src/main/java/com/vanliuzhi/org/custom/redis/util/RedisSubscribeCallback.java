package com.vanliuzhi.org.custom.redis.util;

public interface RedisSubscribeCallback {
    void callback(String msg);
}
