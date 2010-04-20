package org.mashbot.server.types;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="authinfo")
public class UserAuthenticationInformation {
	private String userName;
	private Map<String,ServiceCredential> credentials;
	private Date expiration;
	private int secondsUntilExpiration;
	private static final int defaultSecondsUntilExpiration = 1200;
	
	
	public UserAuthenticationInformation() {
		super();
		new UserAuthenticationInformation("default", new HashMap<String, ServiceCredential>());
		this.userName = "";
		this.credentials = new HashMap<String, ServiceCredential>();
		this.secondsUntilExpiration = defaultSecondsUntilExpiration;
		this.setExpiration(new Date(new Date().getTime()+this.secondsUntilExpiration*1000));
	}
	
	public UserAuthenticationInformation(String userName,
			Map<String, ServiceCredential> credentials){
		new UserAuthenticationInformation(userName,credentials,UserAuthenticationInformation.defaultSecondsUntilExpiration);
		this.userName = userName;
		this.credentials = credentials;
		this.secondsUntilExpiration = this.defaultSecondsUntilExpiration;
		this.setExpiration(new Date(new Date().getTime()+this.secondsUntilExpiration*1000));
	}

	public UserAuthenticationInformation(String userName,
			Map<String, ServiceCredential> credentials, int secondsUntilExpiration) {
		super();
		this.userName = userName;
		this.credentials = credentials;
		this.secondsUntilExpiration = secondsUntilExpiration;
		this.setExpiration(new Date(new Date().getTime()+this.secondsUntilExpiration*1000));
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Map<String, ServiceCredential> getCredentials() {
		return credentials;
	}

	public void setCredentials(Map<String, ServiceCredential> credentials) {
		this.credentials = credentials;
	}
	
	public void addCredential(String service, ServiceCredential credential){
		this.credentials.put(service, credential);
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public Date getExpiration() {
		return expiration;
	}
	
	public void renewLease() {
		renewLease(this.secondsUntilExpiration);
	}
	
	private void renewLease(int secondsUntilExpiration) {
		this.setExpiration(new Date(new Date().getTime()+secondsUntilExpiration*1000));
	}
}
