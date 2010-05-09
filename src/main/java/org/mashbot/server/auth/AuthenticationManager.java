package org.mashbot.server.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mashbot.server.types.ServiceCredential;
import org.mashbot.server.types.UserAuthenticationInformation;

/**
 * This class is a manager for cached third-party service credentials. 
 * Upon receiving a set of credentials, it issues a UUID to uniquely
 * identify that set. Using this id, the credentials may be updated, 
 * listed, renewed or invalidated at any time up until they expired
 * at which point they are removed from the system.
 * 
 * @author Andrew Gall
 *
 */
public class AuthenticationManager {
	private Log log = LogFactory.getLog(getClass());
	private OldAuthenticationCleanupThread cleaner;
	private int cleanupInterval;
	public Map<UUID,UserAuthenticationInformation> tokenCredentials;
	private Map<String,List<UUID>> userToUUID;
	private Lock mapLock;	
	
	/**
	 * Initializes the authentication manager with an empty map and the default
	 * interval in seconds at which each old key cleanup should occur.  
	 */
	public AuthenticationManager(){
		super();
		//new AuthenticationManager(120);
		this.cleanupInterval = 120;
		this.tokenCredentials = new HashMap<UUID,UserAuthenticationInformation>();
		this.userToUUID = new HashMap<String,List<UUID>>();
		this.mapLock = new ReentrantLock();
		//this.cleaner = new OldAuthenticationCleanupThread(this.cleanupInterval);
	}
	
	/**
	 * Allows the user to specify the interval at which each old key cleanup should occur.
	 * @param cleanupInterval - The interval at which cleanup should occur in seconds
	 */
	public AuthenticationManager(int cleanupInterval){
		super();
		this.cleanupInterval = cleanupInterval;
		this.tokenCredentials = new HashMap<UUID,UserAuthenticationInformation>();
		this.userToUUID = new HashMap<String,List<UUID>>();
		this.mapLock = new ReentrantLock();
		//this.cleaner = new OldAuthenticationCleanupThread(this.cleanupInterval);
	}
	
	/**
	 * A completed specified constructor allowing the user to define all fields of the {@link AuthenticationManager}
	 * @param cleanupInterval - The interval at which cleanup should occur in seconds
	 * @param userCredentials - An initial map containing credentials which are each mapped to an UUID
	 * @param userToUUID - An initial map containing lists of UUID's mapped to user names
	 */
	public AuthenticationManager( int cleanupInterval,
			Map<UUID, UserAuthenticationInformation> tokenCredentials, Map<String,List<UUID>> userToUUID ) {
		super();
		this.cleanupInterval = cleanupInterval;
		this.tokenCredentials = tokenCredentials;
		this.userToUUID = userToUUID;
		this.mapLock = new ReentrantLock();
		//this.cleaner = new OldAuthenticationCleanupThread(this.cleanupInterval);
	}
	
	/**
	 * A thread class which runs the cleanup method periodically
	 * @author aeg37
	 *
	 */
	private class OldAuthenticationCleanupThread extends Thread {
		private int cleanupInterval;

		protected OldAuthenticationCleanupThread(int cleanupInterval){
			this.cleanupInterval = cleanupInterval;
		}
		
		public void run(){
			while(true){
				Date start = new Date();
				cleanupOldAuthenticationInfo();
				Date end = new Date();
				try {
					Thread.sleep((cleanupInterval*1000)-(end.getTime()-start.getTime()) > 10000 ? 10000 : (cleanupInterval*1000)-(end.getTime()-start.getTime()) );
				} catch (InterruptedException e) {
					
				}
			}
		}
		
		private Lock timeLock;
	}

	/**
	 * Calling this method allows you to provide a user name and a set of credentials
	 * and be returned a unique identifier which in the future, until expiration of these
	 * credentials can be used to reference those credentials
	 * 
	 * @param username - the user name associated with the credentials
	 * @param credentials - a map containing credentials keyed into such by service name
	 * @return a UUID which uniquely identifies the credentials provided 
	 */
	public String getAuthenticationToken(String username, Map<String,ServiceCredential> credentials){
		UUID id = UUID.randomUUID();
		//UUID id = new UUID(1,1);
		for(Entry<String,ServiceCredential> i : credentials.entrySet()){
			System.out.println(i.getKey());
		}
	    UserAuthenticationInformation userauth = new UserAuthenticationInformation(username,credentials);
	    try{
	    	this.mapLock.lock();
	    	log.warn("USER AUTH:"+userauth.getCredentials());
			this.tokenCredentials.put(id, userauth);
			
			if(this.userToUUID.containsKey(username)){
				this.userToUUID.get(username).add(id);
			} else {
				List<UUID> ids = new ArrayList<UUID>();
				ids.add(id);
					
				this.userToUUID.put(username, ids);
			}
	    } finally {
	    	this.mapLock.unlock();
	    }
	    
	    System.out.println(this.tokenCredentials.get(id).getCredentials());
		return id.toString();
	}
	
	/**
	 * Allows the credentials associated with a token to be updated and added to. 
	 * The new credentials passed in are assumed to better and as such any collisions 
	 * in new and old result in the new overriding the old.
	 * @param token - A UUID which uniquely identifies a set of credential data
	 * @param credentials - A map containing credentials keyed into such by service name
	 * to be added to the existing set of credentials.  
	 * @return the current set of credentials stored after the update operation
	 */
	public UserAuthenticationInformation updateAuthenticationCredentials(UUID token, Map<String,ServiceCredential> credentials){
		
		Map<String,ServiceCredential> currentCreds = new HashMap<String,ServiceCredential>();
		
		UserAuthenticationInformation currentTokenInfo = null;
		
		try{
			mapLock.lock();
			
			if(!removeIfExpired(token)){
				currentTokenInfo = tokenCredentials.get(token);
				currentCreds = currentTokenInfo.getCredentials();
				for(String service : credentials.keySet()){
					currentCreds.put(service, credentials.get(service));
				}
				currentTokenInfo.setCredentials(currentCreds);
				tokenCredentials.put(token,currentTokenInfo);
				log.warn("Current Creds:" + currentCreds);
			} else {
				return null;
			}
		} finally {
			mapLock.unlock();
		}
		
		return currentTokenInfo;
	}
	
	private void removeToken(UUID token) {
		if(tokenCredentials.containsKey(token)){
			UserAuthenticationInformation removed = tokenCredentials.remove(token);
			userToUUID.get(removed.getUserName()).remove(token);
			if(userToUUID.get(removed.getUserName()).size() == 0){
				userToUUID.remove(removed.getUserName());
			}
		}
	}
	
	/**
	 * Invalidates the user credential information associated with the token provided
	 * 
	 * @param token - A UUID which uniquely identifies a set of credential data
	 * @return a boolean which represents the presence or absence of the credentials
	 * to be expired when they are removed
	 */
	public boolean invalidateAuthenticationToken(UUID token){
		try {
			this.mapLock.lock();		
			removeToken(token);
		} finally {
			this.mapLock.unlock();
		}
		return true;
	}
	
	public boolean invalidateAllUserAuthenticationToken(String username){
		try {
			mapLock.lock();
			if(userToUUID.containsKey(username)){
				for(UUID token : userToUUID.get(username)){
					removeToken(token);
				}
			}
		} finally {
			mapLock.unlock();
		}
		return true;
	}
	
	/**
	 * Renews the lease of the user credential information associated with the token provided
	 * 
	 * @param token - A UUID which uniquely identifies a set of credential data
	 * @return the success or failure of the renewal. Will be false if token does
	 * not exist or has expired.
	 */
	public boolean renewAuthenticationTokenLease(UUID token){
		try {
			this.mapLock.lock();		
			tokenCredentials.get(token).renewLease();
		} finally {
			this.mapLock.unlock();
		}
		return true;
	}
	
	/**
	 * Lists all user credential information associated with the token provided
	 * 
	 * @param token - A UUID which uniquely identifies a set of credential data
	 * @return a list of all credentials associated with the provided token
	 */
	public UserAuthenticationInformation listAuthenticationInformation(UUID token){
		//if(!removeIfExpired(token)){
			return tokenCredentials.get(token);
		/*} else {
			return new UserAuthenticationInformation();
		}*/
	}
	
	/**
	 * Removes user authentication if it is expired
	 * @param token - A UUID which uniquely identifies a set of credential data
	 * @return whether the user authentication was expired or not
	 */
	private boolean removeIfExpired(UUID token){
		UserAuthenticationInformation currentTokenInfo = tokenCredentials.get(token);
		System.out.println("keySet:"+tokenCredentials.keySet());
		System.out.println(currentTokenInfo);
		Date now = new Date();
		if(currentTokenInfo != null && currentTokenInfo.getExpiration().before(now)){
			removeToken(token);
			return true;
		} 
		return false;
	}
	
	/**
	 * Cleans up 10 credentials from the cache
	 */
	public void cleanupOldAuthenticationInfo(){
		cleanupOldAuthenticationInfo(Integer.MAX_VALUE);
	}
	
	/**
	 * Examines a specified number of random elements from the cache to
 	 * determine if they should be expired
	 * @param elementsToCheck - number of elements to be examined
	 */
	public void cleanupOldAuthenticationInfo(int elementsToCheck){
		try{
			this.mapLock.lock();
			Date now = new Date();
			
			Collection<UUID> ids;
			
			for(Entry<UUID, UserAuthenticationInformation> id: tokenCredentials.entrySet()){
				removeIfExpired(id.getKey());
			} 
		} finally {
			this.mapLock.unlock();
		}
	}
}
