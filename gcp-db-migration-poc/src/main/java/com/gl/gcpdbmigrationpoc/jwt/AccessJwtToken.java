package com.gl.gcpdbmigrationpoc.jwt;

import io.jsonwebtoken.Claims;

public class AccessJwtToken {

	String token;
	Claims claims;

	public AccessJwtToken(String token, Claims claims) {
		super();
		this.token = token;
		this.claims = claims;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Claims getClaims() {
		return claims;
	}

	public void setClaims(Claims claims) {
		this.claims = claims;
	}
}
