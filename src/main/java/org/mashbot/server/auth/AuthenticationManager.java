package org.mashbot.server.auth;

import java.util.Map;

import org.mashbot.server.types.ServiceCredential;

public class AuthenticationManager {
	public String getAuthenticationToken(String username, Map<String,ServiceCredential> credentials){
		return username;
	}
	
	public Map<String,ServiceCredential> updateAuthenticationCredentials(String token, Map<String,ServiceCredential> credentials){
		return credentials;
	}
	
	public boolean invalidateAuthenticationToken(String token){
		return true;
	}
	
	public Map<String,ServiceCredential> listAuthenticationInformation(String token){
		return null;
	}
}
