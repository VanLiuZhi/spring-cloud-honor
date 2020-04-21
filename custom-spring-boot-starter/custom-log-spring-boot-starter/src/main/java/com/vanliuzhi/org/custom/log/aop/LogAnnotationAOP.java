// package com.vanliuzhi.org.custom.log.aop;
//
// import com.alibaba.fastjson.JSON;
// import com.alibaba.fastjson.JSONObject;
// import com.github.structlog4j.StructLog4J;
// import com.github.structlog4j.json.JsonFormatter;
// import com.open.capacity.common.auth.details.LoginAppUser;
// import com.open.capacity.common.constant.TraceConstant;
// import com.open.capacity.common.model.SysLog;
// import com.open.capacity.common.util.SysUserUtil;
// import com.vanliuzhi.org.custom.log.annotation.LogAnnotation;
// import com.vanliuzhi.org.custom.log.service.LogService;
// import com.vanliuzhi.org.custom.log.util.TraceUtil;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.commons.lang3.StringUtils;
// import org.aspectj.lang.ProceedingJoinPoint;
// import org.aspectj.lang.annotation.Around;
// import org.aspectj.lang.annotation.Aspect;
// import org.aspectj.lang.reflect.MethodSignature;
// import org.slf4j.MDC;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.core.annotation.Order;
// import org.springframework.core.task.TaskExecutor;
//
// import javax.annotation.PostConstruct;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.util.ArrayList;
// import java.util.Date;
// import java.util.List;
// import java.util.concurrent.CompletableFuture;
// import java.util.concurrent.ThreadLocalRandom;
//
// /**
//  * @Description 日志AOP, 标准日志格式logback-spring.xml
//  * 如果开启日志记录，需要多数据配置
//  * @Author VanLiuZhi
//  * @Date 2020-04-19 23:48
//  */
// @Slf4j
// @Aspect
// @Order(-1) // 保证该AOP在@Transactional之前执行
// public class LogAnnotationAOP {
//
//     @Autowired(required = false)
//     private LogService logService;
//
//
//     @Autowired
//     private TaskExecutor taskExecutor;
//
//     @Value("${spring.profiles.active:NA}")
//     private String activeProfile;
//
//     @Value("${spring.application.name:NA}")
//     private String appName;
//
//     @PostConstruct
//     public void init() {
//
//         StructLog4J.setFormatter(JsonFormatter.getInstance());
//
//         StructLog4J.setMandatoryContextSupplier(() -> new Object[]{
//                 "env", activeProfile,
//                 "serviceName", appName
//         });
//
//     }
//
//     @Around("@annotation(ds)")
//     public Object logSave(ProceedingJoinPoint joinPoint, LogAnnotation ds) throws Throwable {
//
//         // 请求流水号
//         String transid = StringUtils.defaultString(TraceUtil.getTrace(), MDC.get(TraceConstant.LOG_TRACE_ID));
//         // 记录开始时间
//         long start = System.currentTimeMillis();
//         // 获取方法参数
//         String url = null;
//         String httpMethod = null;
//         Object result = null;
//         List<Object> httpReqArgs = new ArrayList<Object>();
//         SysLog sysLog = new SysLog();
//         sysLog.setCreateTime(new Date());
//         LoginAppUser loginAppUser = SysUserUtil.getLoginAppUser();
//         if (loginAppUser != null) {
//             sysLog.setUsername(loginAppUser.getUsername());
//         }
//         MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//         LogAnnotation logAnnotation = methodSignature.getMethod().getDeclaredAnnotation(LogAnnotation.class);
//         sysLog.setModule(logAnnotation.module() + ":" + methodSignature.getDeclaringTypeName() + "/"
//                 + methodSignature.getName());
//
//         Object[] args = joinPoint.getArgs();// 参数值
//         url = methodSignature.getDeclaringTypeName() + "/" + methodSignature.getName();
//         String params = null;
//         for (Object object : args) {
//             if (object instanceof HttpServletRequest) {
//                 HttpServletRequest request = (HttpServletRequest) object;
//                 url = request.getRequestURI();
//                 httpMethod = request.getMethod();
//             } else if (object instanceof HttpServletResponse) {
//             } else {
//
//                 httpReqArgs.add(object);
//             }
//         }
//
//         try {
//             params = JSONObject.toJSONString(httpReqArgs);
//             sysLog.setParams(params);
//             // 打印请求参数参数
//             log.info("开始请求，transid={},  url={} , httpMethod={}, reqData={} ", transid, url, httpMethod, params);
//         } catch (Exception e) {
//             log.error("记录参数失败：{}", e.getMessage());
//         }
//
//         try {
//             // 调用原来的方法
//             result = joinPoint.proceed();
//             sysLog.setFlag(Boolean.TRUE);
//         } catch (Exception e) {
//             sysLog.setFlag(Boolean.FALSE);
//             sysLog.setRemark(e.getMessage());
//             log.error("请求报错，transid={},  url={} , httpMethod={}, reqData={} ,error ={} ", transid, url, httpMethod, params, e.getMessage());
//             throw e;
//         } finally {
//
//             // log.info(SecurityContextHolder.getContext().getAuthentication().getPrincipal()+"");
//
//             // 如果需要记录数据库开启异步操作
//             if (logAnnotation.recordRequestParam()) {
//                 CompletableFuture.runAsync(() -> {
//                     try {
//
//                         // log.info(SecurityContextHolder.getContext().getAuthentication().getPrincipal()+"");
//                         log.trace("日志落库开始：{}", sysLog);
//                         if (logService != null) {
//                             logService.save(sysLog);
//                         }
//                         log.trace("开始落库结束：{}", sysLog);
//
//
//                     } catch (Exception e) {
//                         log.error("落库失败：{}", e.getMessage());
//                     }
//
//                 }, taskExecutor);
//             }
//
//             // 获取回执报文及耗时
//             log.info("请求完成, transid={}, 耗时={}, resp={}:", transid, (System.currentTimeMillis() - start),
//                     result == null ? null : JSON.toJSONString(result));
//
//         }
//         return result;
//     }
//
//
//     /**
//      * 生成日志随机数
//      *
//      * @return
//      */
//     public String getRandom() {
//         int i = 0;
//         StringBuilder st = new StringBuilder();
//         while (i < 5) {
//             i++;
//             st.append(ThreadLocalRandom.current().nextInt(10));
//         }
//         return st.toString() + System.currentTimeMillis();
//     }
//
// }
