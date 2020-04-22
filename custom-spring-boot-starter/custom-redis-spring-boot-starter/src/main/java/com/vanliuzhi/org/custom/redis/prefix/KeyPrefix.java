package com.vanliuzhi.org.custom.redis.prefix;

public interface KeyPrefix {

    public int expireSeconds();

    public String getPrefix();

}
