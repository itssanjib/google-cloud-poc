package com.gl.gcpdbmigrationpoc.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix="gcp.db.migr")

public class DBMigrationProperties {

	private String dumpFilePath;
	private String fileType;
	private String database;
	private String grantType;
	private String sqlImportUrl;
}
