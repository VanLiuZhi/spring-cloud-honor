package com.vanliuzhi.org.custom.resource.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.vanliuzhi.org.common.module.constant.UaaConstant;
import com.vanliuzhi.org.custom.resource.server.dao.SysClientDao;
import com.vanliuzhi.org.custom.resource.server.service.SysClientService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description 查询应用绑定的资源权限，就是客户端和系统服务(微服务)的绑定关系
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
@Slf4j
@SuppressWarnings("all")
@Service("sysClientService")
public class SysClientServiceImpl implements SysClientService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // oauth_client_details 表操作 DAO
    @Autowired
    private SysClientDao sysClientDao;

    public Map getClient(String clientId) {
        // 先从redis获取
        Map client = null;
        String value = (String) redisTemplate.boundHashOps(UaaConstant.CACHE_CLIENT_KEY).get(clientId);
        // 没有从数据库获取
        if (StringUtils.isBlank(value)) {
            client = sysClientDao.getClient(clientId);
        } else {
            client = JSONObject.parseObject(value, Map.class);
        }
        return client;
    }

}
