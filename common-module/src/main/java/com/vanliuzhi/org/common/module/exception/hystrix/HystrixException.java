package com.vanliuzhi.org.common.module.exception.hystrix;

import com.netflix.hystrix.exception.HystrixBadRequestException;

/**
 * @Description feign client 避免熔断异常处理
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
public class HystrixException extends HystrixBadRequestException {

    private static final long serialVersionUID = -2437160791033393978L;

    public HystrixException(String msg) {
        super(msg);
    }

    public HystrixException(Exception e) {
        this(e.getMessage());
    }

}
