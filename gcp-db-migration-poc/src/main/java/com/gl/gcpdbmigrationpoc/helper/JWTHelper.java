package com.gl.gcpdbmigrationpoc.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.gl.gcpdbmigrationpoc.constant.CommonConstants;
import com.gl.gcpdbmigrationpoc.exception.ServiceException;
import com.gl.gcpdbmigrationpoc.jwt.AccessJwtToken;
import com.gl.gcpdbmigrationpoc.jwt.JwtSettings;
import com.gl.gcpdbmigrationpoc.jwt.JwtTokenFactory;
import com.gl.gcpdbmigrationpoc.properties.JWTProperties;
import com.gl.gcpdbmigrationpoc.utils.PlatformObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTHelper {

	public static AccessJwtToken createRS256Token(JWTProperties configProperties) throws ServiceException {
		
		try {
						
			JsonNode serviceAccountCredentials = PlatformObjectMapper.fromClassPath(configProperties.getSecretKeyFilePath(), JsonNode.class);
			String shiningKey = serviceAccountCredentials.get(CommonConstants.PRIVATE_KEY).asText();
			String tokenUri = serviceAccountCredentials.get(CommonConstants.TOKEN_URI).asText();
			log.debug("shiningKey" + shiningKey);
			
			JwtSettings settings = new JwtSettings(configProperties.getTokenExpirationTime(), shiningKey);
			JwtTokenFactory factory = new JwtTokenFactory(settings);
			
			Claims claims = Jwts.claims();
			// claims.setSubject("JWT Token");
			claims.put(Claims.ISSUER, configProperties.getIss());
			claims.put(CommonConstants.SCOPE, configProperties.getScope());
			claims.put(Claims.AUDIENCE, tokenUri);
			
			return factory.createAccessJwtToken(SignatureAlgorithm.RS256, claims);
			
		} catch (Exception e) {
			throw new ServiceException("Something went wron in JWTHelper class.");
		}
		
	}

	public static void main(String[] args) {
		try {
			
			log.debug("JWT Token: " + JWTHelper.createRS256Token(null).getToken());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
}