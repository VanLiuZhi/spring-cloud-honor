package com.vanliuzhi.org.common.module.constant;

/**
 * @Description oauth常量类
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
public class UaaConstant {

	 /**
     * 缓存client的redis key，这里是hash结构存储
     */
    public static final String CACHE_CLIENT_KEY = "oauth_client_details";

    public static final String TOKEN_PARAM = "access_token" ;

    public static final String TOKEN_HEADER = "accessToken" ;

    public static final String AUTH = "auth" ;

    public static final String TOKEN = "token" ;

    public static final String AUTHORIZTION = "Authorization" ;

}
