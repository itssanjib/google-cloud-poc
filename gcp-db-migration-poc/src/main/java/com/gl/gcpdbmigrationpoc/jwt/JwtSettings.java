package com.gl.gcpdbmigrationpoc.jwt;

public class JwtSettings {

	private String tokenIssuer;
	private long tokenExpirationTime;
	private String tokenSigningKey;

	public JwtSettings(String tokenIssuer, long tokenExpirationTime, String tokenSigningKey) {
		super();
		this.tokenIssuer = tokenIssuer;
		this.tokenExpirationTime = tokenExpirationTime;
		this.tokenSigningKey = tokenSigningKey;
	}
	
	public JwtSettings(long tokenExpirationTime, String tokenSigningKey) {
		super();
		this.tokenExpirationTime = tokenExpirationTime;
		this.tokenSigningKey = tokenSigningKey;
	}

	public String getTokenIssuer() {
		return tokenIssuer;
	}

	public void setTokenIssuer(String tokenIssuer) {
		this.tokenIssuer = tokenIssuer;
	}

	public long getTokenExpirationTime() {
		return tokenExpirationTime;
	}

	public void setTokenExpirationTime(long tokenExpirationTime) {
		this.tokenExpirationTime = tokenExpirationTime;
	}

	public String getTokenSigningKey() {
		return tokenSigningKey;
	}

	public void setTokenSigningKey(String tokenSigningKey) {
		this.tokenSigningKey = tokenSigningKey;
	}
}
