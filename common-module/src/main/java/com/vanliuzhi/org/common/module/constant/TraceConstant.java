package com.vanliuzhi.org.common.module.constant;

/**
 * @Description Trace常量类
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
public class TraceConstant {
    /**
     * 日志跟踪id名。
     */
    public static final String LOG_TRACE_ID = "traceId";

    // TODO 在网关和拦截器中有用到，记得把旧名字改成新的
    public static final String LOG_B3_TRACE_ID = "X-B3-TraceId";

    /**
     * 请求头跟踪id名。
     */
    public static final String HTTP_HEADER_TRACE_ID = "app_trace_id";

}
