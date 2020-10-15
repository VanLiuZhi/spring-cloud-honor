package vanliuzhi.org.auth.service.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**获取验证码
 * @author VanLiuZhi
 * @date 2020/10/15 23:03
 */
@Controller
public class ValidateCodeController {
    @Autowired
    private Producer producer;

    // @Autowired
    // private ValidateCodeService validateCodeService;
}
