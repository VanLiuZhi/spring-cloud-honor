package vanliuzhi.org.order.server.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vanliuzhi.org.auth.client.model.CurrentUser;
import vanliuzhi.org.auth.client.utils.SecurityUtils;
import vanliuzhi.org.order.server.feign.UserFeignApi;

/**
 * 功能描述:
 *
 * @author Trazen
 * @date 2020/7/15 16:16
 */
@RestController
@RequestMapping("/order")
@Slf4j
@AllArgsConstructor
public class OrderController {

    private final UserFeignApi userFeignApi;

    @GetMapping("/a")
    public String get02(@AuthenticationPrincipal CurrentUser currentUser) {
        log.info("---------------->{}", SecurityUtils.getCurrentUser());
        return "1";
    }

    @GetMapping("/b")
    public String getFeign() {
        return userFeignApi.get02();
    }
}
