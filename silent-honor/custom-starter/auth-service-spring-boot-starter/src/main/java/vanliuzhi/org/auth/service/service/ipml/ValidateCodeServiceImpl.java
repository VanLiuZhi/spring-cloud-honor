package vanliuzhi.org.auth.service.service.ipml;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import vanliuzhi.org.auth.service.service.ValidateCodeService;
import vanliuzhi.org.common.starter.utils.CommonStringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author Lys3415
 * @date 2020/10/16 11:22
 */
@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private String buildKey(String deviceId) {
        return "DEFAULT_CODE_KEY:" + deviceId;
    }

    /**
     * 保存图形验证码到redis中，过期时间为5分钟
     *
     * @param deviceId  前端唯一标识
     * @param imageCode 验证码
     */
    @Override
    public void saveImageCode(String deviceId, String imageCode) {
        redisTemplate.execute((RedisCallback<String>) connection -> {
            connection.set(buildKey(deviceId).getBytes(), imageCode.getBytes());
            connection.expire(buildKey(deviceId).getBytes(), 60L * 5L);
            connection.close();
            return imageCode;
        });
    }

    /**
     * 获取验证码
     *
     * @param deviceId 前端唯一标识/手机号
     */
    @Override
    public String getCode(String deviceId) {
        String code = "";
        try {
            code = redisTemplate.execute((RedisCallback<String>) connection -> {
                byte[] temp;
                temp = connection.get(buildKey(deviceId).getBytes());
                connection.close();
                return String.valueOf(Optional.ofNullable(temp));
            });
        } catch (Exception e) {
            throw new AuthenticationException("验证码不存在!") {
            };
        }
        return code;
    }

    /**
     * 删除验证码
     *
     * @param deviceId 前端唯一标识/手机号
     */
    @Override
    public void remove(String deviceId) {
        redisTemplate.execute((RedisCallback<String>) connection -> {
            connection.del(buildKey(deviceId).getBytes());
            connection.close();
            return "";
        });
    }

    /**
     * 验证验证码，从 HttpServletRequest 获取传递的验证码，和存储位置的验证码做比较
     */
    @Override
    public void validate(HttpServletRequest request) {
        String deviceId = request.getParameter("deviceId");
        if (CommonStringUtil.isBlank(deviceId)) {
            throw new AuthenticationException("请在请求参数中携带deviceId参数") {
            };
        }
        String code = this.getCode(deviceId);
        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request, "validCode");
        } catch (ServletRequestBindingException e) {
            throw new AuthenticationException("获取验证码的值失败") {
            };
        }
        if (StringUtils.isBlank(codeInRequest)) {
            throw new AuthenticationException("请填写验证码") {
            };
        }
        if (code == null) {
            throw new AuthenticationException("验证码不存在或已过期") {
            };
        }
        if (!StringUtils.equalsIgnoreCase(code, codeInRequest)) {
            throw new AuthenticationException("验证码不正确") {
            };
        }
        // 获取验证码后，从存储位置移除
        this.remove(deviceId);
    }

    /**
     * 传递一个验证码，通过deviceId获取到存储位置的验证码，两个进行比较
     */
    @Override
    public void validate(String deviceId, String validCode) {
        if (CommonStringUtil.isBlank(deviceId)) {
            throw new AuthenticationException("请在请求参数中携带deviceId参数") {
            };
        }
        String code = this.getCode(deviceId);
        if (CommonStringUtil.isBlank(validCode)) {
            throw new AuthenticationException("请填写验证码") {
            };
        }
        if (code == null) {
            throw new AuthenticationException("验证码不存在或已过期") {
            };
        }
        if (!StringUtils.equalsIgnoreCase(code, validCode)) {
            throw new AuthenticationException("验证码不正确") {
            };
        }
        this.remove(deviceId);
    }

}
