package org.mashbot.server.auth;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.mashbot.server.types.ServiceCredential;
import org.mashbot.server.types.UserAuthenticationInformation;
import org.springframework.transaction.annotation.Transactional;

import sun.misc.Cleaner;

public class AuthenticationManager {
	public AuthenticationManager(){
		new AuthenticationManager(new ConcurrentHashMap<UUID, UserAuthenticationInformation>());
	}
	
	public AuthenticationManager(
			Map<UUID, UserAuthenticationInformation> userCredentials) {
		super();
		this.tokenCredentials = userCredentials;
		this.mapLock = new ReentrantLock();
	}

	public String getAuthenticationToken(String username, Map<String,ServiceCredential> credentials){
		UUID id = UUID.randomUUID();
	    UserAuthenticationInformation userauth = new UserAuthenticationInformation(username,credentials);
		
	    try{
	    	mapLock.lock();
	    	
			this.tokenCredentials.put(id, userauth);
			
			if(this.userToUUID.containsKey(username)){
				this.userToUUID.get(username).add(id);
			} else {
				List<UUID> ids = new ArrayList<UUID>();
				ids.add(id);
					
				this.userToUUID.put(username, ids);
			}
	    } finally {
	    	mapLock.unlock();
	    }
		return id.toString();
	}
	
	public Map<String,ServiceCredential> updateAuthenticationCredentials(String token, Map<String,ServiceCredential> credentials){
		UUID id = UUID.fromString(token);
		
		Map<String,ServiceCredential> currentCreds = new HashMap<String,ServiceCredential>();
		
		try{
			mapLock.lock();
			
			if(!removeIfExpired(id)){
				UserAuthenticationInformation currentTokenInfo = tokenCredentials.get(id);
				currentCreds = currentTokenInfo.getCredentials();
				
				for(String service : credentials.keySet()){
					currentCreds.put(service, credentials.get(service));
				}
			}
		} finally {
			mapLock.unlock();
		}
		
		return currentCreds;
	}
	
	public boolean invalidateAuthenticationToken(String token){
		UUID id = UUID.fromString(token);
		try {
			mapLock.lock();		
			tokenCredentials.remove(id);
		} finally {
			mapLock.unlock();
		}
		return true;
	}
	
	public Map<String,ServiceCredential> listAuthenticationInformation(String token){
		UUID id = UUID.fromString(token); 
		if(!removeIfExpired(id)){
			return tokenCredentials.get(id).getCredentials();
		} else {
			return new HashMap<String,ServiceCredential>();
		}
	}
	
	private boolean removeIfExpired(UUID id){
		UserAuthenticationInformation currentTokenInfo = tokenCredentials.get(id);
		Date now = new Date();
		if(currentTokenInfo.getExpiration().before(now)){
			tokenCredentials.remove(id);
			return true;
		} 
		return false;
	}
	
	public void cleanupOldAuthenticationInfo(){
		cleanupOldAuthenticationInfo(10);
	}
	
	public void cleanupOldAuthenticationInfo(int elementsToCheck){
		try{
			mapLock.lock();
			Date now = new Date();
			
			List<UUID> ids;
			
			if(tokenCredentials.keySet().size() > elementsToCheck){
				Random rand = new Random(System.currentTimeMillis());
				ids = new ArrayList(elementsToCheck);
				for(int i = 0; i < elementsToCheck; i++){
					ids.add()
				}
			}
			for(UUID id : tokenCredentials.keySet()){
				if(tokenCredentials.get(id).getExpiration().before(now)){
					tokenCredentials.remove(id);
				}
			}
		} finally {
			mapLock.unlock();
		}
	}
	
	
	Map<UUID,UserAuthenticationInformation> tokenCredentials;
	Map<String,List<UUID>> userToUUID;
	Lock mapLock;
}
