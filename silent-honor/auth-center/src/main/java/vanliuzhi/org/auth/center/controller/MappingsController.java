package vanliuzhi.org.auth.center.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author VanLiuZhi
 * @date 2020/9/29 11:28
 */
@Controller
public class MappingsController {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @RequestMapping(value = "/mappings")
    public List<HashMap<String, String>> list(Model model) {
        List<HashMap<String, String>> urlList = new ArrayList<>();

        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            HashMap<String, String> hashMap = new HashMap<>(8);
            RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            PatternsRequestCondition p = info.getPatternsCondition();
            for (String url : p.getPatterns()) {
                hashMap.put("url", url);
            }
            // 类名
            hashMap.put("className", method.getMethod().getDeclaringClass().getName());
            // 方法名
            hashMap.put("method", method.getMethod().getName());
            RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
            String type = methodsCondition.toString();
            if (type.startsWith("[") && type.endsWith("]")) {
                type = type.substring(1, type.length() - 1);
                // 方法名
                hashMap.put("type", type);
            }
            urlList.add(hashMap);
        }
        model.addAttribute("list", urlList);
        return urlList;
    }
}
