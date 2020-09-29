package vanliuzhi.org.test.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lys3415
 * @date 2020/9/29 10:44
 */
@Slf4j
@RestController
public class OrderController {

    @GetMapping("test")
    public String test() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(principal.toString());
        return principal.toString();
    }

}
