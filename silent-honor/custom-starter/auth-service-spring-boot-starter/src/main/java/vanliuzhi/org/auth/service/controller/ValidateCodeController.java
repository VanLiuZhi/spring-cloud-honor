package vanliuzhi.org.auth.service.controller;

import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vanliuzhi.org.auth.service.service.ValidateCodeService;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 生成验证码
 *
 * @author VanLiuZhi
 * @date 2020/10/15 23:03
 */
@Slf4j
@Controller
public class ValidateCodeController {

    /**
     * 通过配置producer的一些参数后，可以调用创建验证码的接口，获取 string 验证码 和 图片数据
     * 图片数据传递给前端，string存储起来做验证
     */
    @Autowired
    private Producer producer;

    @Autowired
    private ValidateCodeService validateCodeService;

    /**
     * 请求接口，获取一个验证码
     */
    @GetMapping("/validata/code/{deviceId}")
    public void createCode(@PathVariable String deviceId, HttpServletResponse response) throws Exception {
        Assert.notNull(deviceId, "机器码不能为空");
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        validateCodeService.saveImageCode(deviceId, text);
        // 打开io处理
        try (ServletOutputStream out = response.getOutputStream()) {
            // 写io，try语句会自动关闭io
            ImageIO.write(image, "JPEG", out);
        } catch (IOException e) {
            log.error("验证码流处理异常");
        }
    }
}
