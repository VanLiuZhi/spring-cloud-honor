package vanliuzhi.org.common.starter.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lys3415
 * @date 2020/10/13 10:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private T data;
    private Integer code;
    private String msg;

    /**
     * 返回成功，没有数据
     */
    public static <T> Result<T> succeed(String msg) {
        return succeedWith(null, CodeEnum.SUCCESS.getCode(), msg);
    }

    /**
     * 返回成功，需要传递data
     */
    public static <T> Result<T> succeed(T data, String msg) {
        return succeedWith(data, CodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeedWith(T data, Integer code, String msg) {
        return new Result<T>(data, code, msg);
    }

    /**
     * 返回失败，没有数据
     */
    public static <T> Result<T> failed(String msg) {
        return failedWith(null, CodeEnum.ERROR.getCode(), msg);
    }

    public static <T> Result<T> failed(T data, String msg) {
        return failedWith(data, CodeEnum.ERROR.getCode(), msg);
    }

    public static <T> Result<T> failedWith(T data, Integer code, String msg) {
        return new Result<T>(data, code, msg);
    }
}
