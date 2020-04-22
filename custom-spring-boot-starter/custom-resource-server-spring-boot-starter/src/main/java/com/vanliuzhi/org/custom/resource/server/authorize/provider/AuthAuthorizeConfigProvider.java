package com.vanliuzhi.org.custom.resource.server.authorize.provider;

import com.vanliuzhi.org.common.module.auth.props.PermitUrlProperties;
import com.vanliuzhi.org.custom.resource.server.authorize.AuthorizeConfigProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * @Description 白名单
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
@Component
@Order(Integer.MAX_VALUE - 1)
@EnableConfigurationProperties(PermitUrlProperties.class)
public class AuthAuthorizeConfigProvider implements AuthorizeConfigProvider {

	@Autowired(required = false)
	private PermitUrlProperties permitUrlProperties;

	@Override
	public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {

		// 免token登录设置
		config.antMatchers(permitUrlProperties.getIgnored()).permitAll();
		config.requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll(); // 监控断点放权
		// 前后分离时需要带上
		config.antMatchers(HttpMethod.OPTIONS).permitAll();

		return true;
	}

}
