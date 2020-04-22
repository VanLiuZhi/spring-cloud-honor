package com.vanliuzhi.org.common.module.exception.dao;

import com.vanliuzhi.org.common.module.exception.BaseException;

/**
 * @Description 数据库相关异常
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
public class DataAccessException extends BaseException {

    private static final long serialVersionUID = 8325096920926406459L;

    public DataAccessException(String msg) {
        super(msg);
    }

    public DataAccessException(Exception e) {
        this(e.getMessage());
    }

}
