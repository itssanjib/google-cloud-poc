package com.gl.gcpdbmigrationpoc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

	@Bean
	RestTemplate create() {
		return new RestTemplate();
	}
}
