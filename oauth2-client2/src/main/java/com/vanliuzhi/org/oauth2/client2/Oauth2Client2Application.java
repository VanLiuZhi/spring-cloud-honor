package com.vanliuzhi.org.oauth2.client2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

@SpringBootApplication
@EnableOAuth2Sso
public class Oauth2Client2Application {

	public static void main(String[] args) {
		SpringApplication.run(Oauth2Client2Application.class, args);
	}

}
