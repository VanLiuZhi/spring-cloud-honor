package com.vanliuzhi.org.common.module.feign;

import feign.Logger.Level;
import org.springframework.context.annotation.Bean;

public class GlobalFeignConfig {

	@Bean
	public Level level(){
		return Level.FULL;
	}

}
