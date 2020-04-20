package com.vanliuzhi.org.oauth2.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

@SpringBootApplication
@EnableOAuth2Sso
public class Oauth2ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(Oauth2ClientApplication.class, args);
	}

}
