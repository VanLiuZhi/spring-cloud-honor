package com.vanliuzhi.org.common.module.exception;

/**
 * @Description 基本异常，系统定义的所有异常都需要继承这个基本类
 * RuntimeException 也从父级继承 Serializable，所以定义的异常类都是可序列化的
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 7859712770754900356L;

    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(Exception e) {
        this(e.getMessage());
    }

}
