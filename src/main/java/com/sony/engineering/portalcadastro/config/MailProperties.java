package com.sony.engineering.portalcadastro.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mail")
public class MailProperties {
	
	private String username;
	
	private String tokenDir;
	
	private String credentials;

	public String getTokenDir() {
		return tokenDir;
	}

	public void setTokenDir(String tokenDir) {
		this.tokenDir = tokenDir;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
