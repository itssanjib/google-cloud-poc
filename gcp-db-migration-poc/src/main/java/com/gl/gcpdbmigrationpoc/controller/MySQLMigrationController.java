package com.gl.gcpdbmigrationpoc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.gl.gcpdbmigrationpoc.constant.CommonConstants;
import com.gl.gcpdbmigrationpoc.exception.ServiceException;
import com.gl.gcpdbmigrationpoc.helper.JWTHelper;
import com.gl.gcpdbmigrationpoc.jwt.AccessJwtToken;
import com.gl.gcpdbmigrationpoc.model.ImportContext;
import com.gl.gcpdbmigrationpoc.model.ImportContextWrapper;
import com.gl.gcpdbmigrationpoc.properties.DBMigrationProperties;
import com.gl.gcpdbmigrationpoc.properties.JWTProperties;
import com.gl.gcpdbmigrationpoc.utils.PlatformObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController("/migrate/mysql")
@Slf4j
public class MySQLMigrationController {
	
	@Autowired
	JWTProperties configProperties;
	
	@Autowired
	DBMigrationProperties dbMigrationProperties;
	
	@Autowired
	RestTemplate restTemplate;

	@RequestMapping(name = "/sql_dump")
	public @ResponseBody JsonNode migrateMySQLUsingSQLDump() {

		log.info("Starting MySQL database migration.");
		
		try {
			AccessJwtToken jwtToken = JWTHelper.createRS256Token(configProperties);
			log.info("JWT token generated: {}", jwtToken.getToken());
			
			// Google ACCESS TOKEN call.
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			
			MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
			map.add(CommonConstants.GRANT_TYPE, dbMigrationProperties.getGrantType());
			map.add(CommonConstants.ASSERTION, jwtToken.getToken());
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			
			ResponseEntity<JsonNode> response = restTemplate.postForEntity(jwtToken.getClaims().getAudience(), request, JsonNode.class);
			JsonNode resBody = response.getBody();
			String accessToken = resBody.get("access_token").asText();
			log.info("Response body for post call {} is {}", jwtToken.getClaims().getAudience(), resBody);
			
			
			// Google IMPORT API call
			
			// Step1: Import the sql dump file from cloud storage to cloud sql.
			// Creating model object
			ImportContextWrapper importContextWrapper = new ImportContextWrapper();
			ImportContext importContext = new ImportContext();
			importContext.setDatabase(dbMigrationProperties.getDatabase());
			importContext.setFileType(dbMigrationProperties.getFileType());
			importContext.setUri(dbMigrationProperties.getDumpFilePath());
			importContextWrapper.setImportContext(importContext);
			
			// Adding headers
			headers.clear();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Authorization", "Bearer " + accessToken);
			HttpEntity<String> entity = new HttpEntity<>(PlatformObjectMapper.mapToJson(importContextWrapper), headers);

			ResponseEntity<JsonNode> resp = restTemplate.postForEntity(dbMigrationProperties.getSqlImportUrl(), entity, JsonNode.class);
			JsonNode respBody = resp.getBody();
			log.info("Response body for POST call {} is {}", dbMigrationProperties.getSqlImportUrl(), respBody);
			log.info("Successfully done MySQL database migration.");
			return respBody;
			
		} catch(ServiceException e) {
			log.error("Service exceptin occurred", e);
			return null;
		}
		
		
//		public @ResponseBody JsonNode migrateMySQLUsingSQLDump(@RequestBody JsonNode requestJSON,
//				@RequestHeader(value = "Accept") String acceptHeader,
//				@RequestHeader(value = "Authorization") String authorizationHeader,
//				@RequestParam(name = "bucket_name") String bucketName,
//				@RequestParam(name = "path_to_dump_file") String pathToDumpFile,
//				@RequestParam(name = "db_name") String dbName,
//				@RequestParam(name = "file_type") String fileType) {
//			
//		}
		
	}
}
