package org.mashbot.server.types;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttachmentRef;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mashbot.server.xml.AllCredentials;
import org.mashbot.server.xml.AllServiceCredentials;
import org.mashbot.server.xml.ServiceCredentialMapAdapter;

@XmlRootElement(name="authinfo")
@XmlSeeAlso(value={AllCredentials.class,AllServiceCredentials.class,HashMap.class,ArrayList.class,ServiceCredential.class})
public class UserAuthenticationInformation {
	private String userName;
	private Map<String, List<ServiceCredential>> credentials;
	private Date expiration;
	private int secondsUntilExpiration;
	private Log log = LogFactory.getLog(getClass());
	private static final int defaultSecondsUntilExpiration = 1200;
	
	
	public UserAuthenticationInformation() {
		super();
		this.userName = "";
		this.credentials = new HashMap<String, List<ServiceCredential>>();
		this.secondsUntilExpiration = defaultSecondsUntilExpiration;
		this.setExpiration(new Date(new Date().getTime()+this.secondsUntilExpiration*1000));
	}
	
	public UserAuthenticationInformation(String userName,
			Map<String, List<ServiceCredential>> credentials){
		this.userName = userName;
		this.credentials = credentials;
		this.secondsUntilExpiration = this.defaultSecondsUntilExpiration;
		this.setExpiration(new Date(new Date().getTime()+this.secondsUntilExpiration*1000));
	}

	public UserAuthenticationInformation(String userName,
			Map<String, List<ServiceCredential>> credentials, int secondsUntilExpiration) {
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
	
	/*@XmlElements({
		@XmlElement(name = "service")
        @XmlAttribute(name = "key",     type = String.class),
        @XmlAttribute(name = "secret", type = String.class),
        @XmlAttribute(name = "method",     type = String.class)
    })*/
	
    /*@XmlJavaTypeAdapter(value=ServiceCredentialMapAdapter.class,type=Map.class)*/
	@XmlTransient
	public Map<String, List<ServiceCredential>> getCredentials() {
		log.warn("HEY:"+this.credentials.get("twitter").get(0).key);
		return this.credentials;
	}

	public void setCredentials(Map<String, List<ServiceCredential>> credentials) {
		this.credentials = credentials;
	}
	
	public void addCredential(String service, List<ServiceCredential> credentials){
		if(this.credentials.containsKey(service)){
			for(ServiceCredential i : credentials){
				this.credentials.get(service).add(i);
			}
		} else {
			this.credentials.put(service, credentials);
		}
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
	
	private List<AllServiceCredentials> authInfo;

	public List<AllServiceCredentials> getAuthInfo() {
		try {
			this.authInfo = awesome.marshal(this.credentials);
		} catch (Exception e) {
			this.authInfo = new ArrayList<AllServiceCredentials>();
		}
				
		return this.authInfo;
	}

	public void setAuthInfo(List<AllServiceCredentials> authInfo) {
		this.authInfo = authInfo;
		try {
			this.credentials = awesome.unmarshal(authInfo);
		} catch (Exception e) {
			this.credentials = new HashMap<String, List<ServiceCredential>>();
		}
	}
	
	private static ServiceCredentialMapAdapter awesome = new ServiceCredentialMapAdapter();
}
