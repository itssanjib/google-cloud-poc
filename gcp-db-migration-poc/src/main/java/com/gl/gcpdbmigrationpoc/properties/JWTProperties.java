package com.gl.gcpdbmigrationpoc.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "jwt.claims")
public class JWTProperties {

	private String iss;
	private String scope;
	private long tokenExpirationTime;
	private String secretKeyFilePath;
}
