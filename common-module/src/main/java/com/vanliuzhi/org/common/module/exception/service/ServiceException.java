package com.vanliuzhi.org.common.module.exception.service;

import com.vanliuzhi.org.common.module.exception.BaseException;

/**
 * @Description service处理异常
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
public class ServiceException extends BaseException {

    private static final long serialVersionUID = -2437160791033393978L;

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(Exception e) {
        this(e.getMessage());
    }
}
