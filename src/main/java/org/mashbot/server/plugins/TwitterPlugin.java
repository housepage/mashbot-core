package org.mashbot.server.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.ServiceCredential;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;


public class TwitterPlugin extends Plugin {
    private static final String serviceName = "twitter";
    private Log log = LogFactory.getLog(getClass());
	private Map<String, List<String>> supported;
	
	public TwitterPlugin(){
		this.supported = new HashMap<String,List<String>>();
		List<String> supportedStatus = new ArrayList<String>();
		supportedStatus.add("push");
		supportedStatus.add("pull");
		supportedStatus.add("edit");
		supportedStatus.add("delete");
		this.supported.put("status", supportedStatus);
	}

	public MObject run(String operation, String contentType, MObject content, ServiceCredential credential) {
        if ( operation.equals("push") && contentType.equals("status")){
            postStatus(content, credential);
        }
        return null;
    }

    public boolean hasRequiredInformation(String operation, String contentType, MObject content){
        return true;
    }

    public List<String> getRequiredInformation(String operation, String contentType){
        return null;
    }

    public String getServiceName(){
        return serviceName;
    }

    public Map<String, List<String>> getSupported(){
        return this.supported;
    }

    private void postStatus(MObject object, ServiceCredential credential){
    	String key = credential.key;
    	String secret = credential.secret;
    	String latestStatus;
    	try{
    		latestStatus = ((ElementNSImpl) object.getField("status")).getTextContent();
    	} catch (Exception e) {
    		log.warn(object.getField("status"));
    		log.warn(object.getFields());
			latestStatus = "Ummmmmm" + new Random().nextInt(); 
		}
    	
    	log.warn("key:"+key+" secret:"+secret);
    	Twitter twitter;
    	TwitterFactory fact = new TwitterFactory();
    	twitter = fact.getOAuthAuthorizedInstance("wOrsK4bFG0VJNplruzVoNg","E3akoP3oG8iQj1XHP9bArQSwlCKfyedPNaFX4U8vd4",new AccessToken(key,secret));
    	log.warn("TWITTER:"+twitter+" "+fact);
    	
    	/*if(credential.method == "userpass"){
    		twitter = new TwitterFactory().getInstance(twitterID, twitterPassword);
    	} else if(credential.method == ""){
    		
    	}*/
        
    	try {
			twitter.updateStatus(latestStatus);
		} catch (TwitterException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		}
        
        log.info("Successfully updated the status to [" + latestStatus + "].");
    }
    
	public enum Property{
		STATUS,
		BLOG 
	}
	
	public static void main(String[] args){
		MObject object = new MObject();
		object.putField("status", "OMG BETA TONIGHT!");
		ServiceCredential mashbot = new ServiceCredential();
		/*mashbot.putField("username", "MashBot");
		mashbot.putField("password", "w1sLm2");*/
		mashbot.key = "MashBot";
		mashbot.secret = "w1sLm2";
		
		TwitterPlugin plugin = new TwitterPlugin();
		//plugin.setFactory(new TwitterFactory());
		plugin.run("push", "status", object, mashbot);
	}
	
	private TwitterFactory factory;

	public TwitterFactory getFactory() {
		return factory;
	}

	public void setFactory(TwitterFactory factory) {
		this.factory = factory;
	}
}
	

