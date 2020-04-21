package com.vanliuzhi.org.custom.log.dto;

import com.github.structlog4j.IToLog;
import lombok.Builder;
import lombok.Data;

/**
 * @Description 审计日志
 * @Author VanLiuZhi
 * @Date 2020-04-19 23:48
 */
@Data
@Builder
public class LogEntry implements IToLog {

    private String webApp;
    private String username;
    private String token;
    private String serviceName;
    private String serviceUri;
    private String requestStr;
    private String responseStr;

    @Override
    public Object[] toLog() {
        return new Object[]{
                "auditlog", "true",
                "webApp", webApp,
                "username", username,
                "token", token,
                "serviceName", serviceName,
                "serviceUri", serviceUri,
                "token", token,
                "requestStr", requestStr,
                "responseStr", responseStr
        };
    }
}
