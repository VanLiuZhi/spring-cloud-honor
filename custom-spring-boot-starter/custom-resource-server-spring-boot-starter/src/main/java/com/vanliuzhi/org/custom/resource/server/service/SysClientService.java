package com.vanliuzhi.org.custom.resource.server.service;

import java.util.Map;

/**
 * @Description 客户端服务
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
@SuppressWarnings("all")
public interface SysClientService {

	/**
	 * 查询应用绑定的资源权限
	 */
	public Map getClient(String clientId);

}
