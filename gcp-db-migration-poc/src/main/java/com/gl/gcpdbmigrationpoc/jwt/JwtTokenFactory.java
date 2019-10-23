package com.gl.gcpdbmigrationpoc.jwt;

import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

import com.gl.gcpdbmigrationpoc.constant.CommonConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenFactory {
	private final JwtSettings settings;

	public JwtTokenFactory(JwtSettings settings) {
		this.settings = settings;
	}

	public AccessJwtToken createAccessJwtToken(SignatureAlgorithm signatureAlgorithm, Claims claims) {

		LocalDateTime localDateTime = LocalDateTime.now();
		Date currentTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		Date expTime = Date.from(localDateTime.plusMinutes(settings.getTokenExpirationTime())
				.atZone(ZoneId.systemDefault()).toInstant());

		try {
			String privateKeyContent = settings.getTokenSigningKey();

			privateKeyContent = privateKeyContent.replaceAll(CommonConstants.NEWLINE_CHAR, CommonConstants.BLANK_STRING)
					.replace(CommonConstants.BEGIN_PRIVATE_KEY, CommonConstants.BLANK_STRING)
					.replace(CommonConstants.END_PRIVATE_KEY, CommonConstants.BLANK_STRING);

			PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));

			final String token = Jwts.builder().setClaims(claims).setIssuedAt(currentTime).setExpiration(expTime)
					.signWith(signatureAlgorithm,
							KeyFactory.getInstance(signatureAlgorithm.getFamilyName()).generatePrivate(keySpecPKCS8))
					.compact();

			return new AccessJwtToken(token, claims);

		} catch (Exception e) {
			log.error("Exception occurred during token generation.", e);
		}

		return null;
	}
}
