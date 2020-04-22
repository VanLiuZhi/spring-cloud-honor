package com.vanliuzhi.org.common.module.exception.controller;

import com.vanliuzhi.org.common.module.exception.BaseException;

/**
 * @Description Controller异常
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
public class ControllerException extends BaseException {

    private static final long serialVersionUID = 1412104290896291466L;

    public ControllerException(String msg) {
        super(msg);
    }

    public ControllerException(Exception e) {
        this(e.getMessage());
    }

}
