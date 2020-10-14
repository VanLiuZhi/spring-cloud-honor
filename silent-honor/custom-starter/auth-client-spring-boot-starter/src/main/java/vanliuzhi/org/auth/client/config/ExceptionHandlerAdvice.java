package vanliuzhi.org.auth.client.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 异常捕获处理
 * <code>@ResponseStatus</code> 注解能够改变响应状态码，通常异常捕获后返回是200的，可以用这个注解修改
 * 如果配置reason，异常捕获的结果将被reason覆盖
 * 也可以配置在类上，这样各种异常都不再需要写响应数据，直接由reason决定
 *
 * @author Lys3415
 * @date 2020/10/13 10:53
 */
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> badRequestException(IllegalArgumentException illegalArgumentException) {
        HashMap<String, Object> objectObjectHashMap = new HashMap<>(2);
        objectObjectHashMap.put("code", HttpStatus.BAD_REQUEST.value());
        objectObjectHashMap.put("msg", illegalArgumentException.getMessage());
        return objectObjectHashMap;
    }
}
