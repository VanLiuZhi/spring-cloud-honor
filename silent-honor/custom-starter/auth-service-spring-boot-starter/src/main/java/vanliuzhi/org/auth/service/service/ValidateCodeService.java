package vanliuzhi.org.auth.service.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author VanLiuZhi
 * @date 2020/10/15 23:05
 */
public interface ValidateCodeService {
    /**
     * 保存图形验证码
     *
     * @param deviceId  前端唯一标识
     * @param imageCode 验证码
     */
    void saveImageCode(String deviceId, String imageCode);

    /**
     * 获取验证码
     *
     * @param deviceId 前端唯一标识/手机号
     * @return 验证码
     */
    String getCode(String deviceId);

    /**
     * 删除验证码
     *
     * @param deviceId 前端唯一标识/手机号
     */
    void remove(String deviceId);

    /**
     * 验证验证码，从 HttpServletRequest 获取传递的验证码，和存储位置的验证码做比较
     *
     * @param request HttpServletRequest
     */
    void validate(HttpServletRequest request);

    /**
     * 传递一个验证码，通过deviceId获取到存储位置的验证码，两个进行比较
     *
     * @param deviceId  机器码
     * @param validCode 验证码
     */
    void validate(String deviceId, String validCode);
}
