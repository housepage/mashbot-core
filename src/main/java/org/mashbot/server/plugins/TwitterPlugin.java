package org.mashbot.server.plugins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.ServiceCredential;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;

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
		
		MObject returnValue = content;
		
		if(contentType.equals("status")){
				if ( operation.equals("push")){
					postStatus(content, credential);
				}
				if ( operation.equals("pull")){
					returnValue = pullStatus(content, credential);
				}
				if ( operation.equals("delete")){
					deleteStatus(content, credential);
				}
		}
		return returnValue;
    }

    public List<String> getRequiredInformation(String operation, String contentType){
    	List<String> requiredInformation = new ArrayList<String>();
    	
    	if(contentType.equals("status")){
    		if (operation.equals("push")){
    			requiredInformation.add("status");
    		}
    		if (operation.equals("pull")){
    			requiredInformation.add("statusId");
    		}
    		if (operation.equals("delete")){
    			requiredInformation.add("statusId");
    		}
    	}
    	return requiredInformation;
    }

    public String getServiceName(){
        return serviceName;
    }

    public Map<String, List<String>> getSupported(){
<<<<<<< HEAD
        Map<String, List<String>> supported = new HashMap<String, List<String>>();
        List<String> operations = Arrays.asList(new String[] {"push", "pull", "delete"});
        supported.put("status", operations);
        return supported;
    }

    private void postStatus(MObject object, ServiceCredential credential){
    	String latestStatus = ((ElementNSImpl) object.getField("STATUS")).getTextContent();

      Twitter twitter = getTwitter(credential);
    	
      Status status;

			try {
				status = twitter.updateStatus(latestStatus);
				System.out.println("Successfully updated the status to [" + status.getText() + "].");
			} catch (TwitterException e) {
				e.printStackTrace();
			}     
    }
    
    private MObject pullStatus(MObject object, ServiceCredential credential){
    	Twitter twitter = getTwitter(credential); 
    	MObject retObject = new MObject();
    	
    	try {
    		long statusId = Long.parseLong((String) object.getField("statusId"));
    		Status s = twitter.showStatus(statusId);
    		String status = s.getText();
    		String user = s.getUser().getName();
    		retObject.putField("status", status);
    		retObject.putField("user", user);
    	}
    	catch (TwitterException e) {
    		e.printStackTrace();
    	}
    	
    	return retObject;
    }
    
    private void deleteStatus(MObject object, ServiceCredential credential){
    	Twitter twitter = getTwitter(credential);
    	
    	try {
    		long statusId = Long.parseLong((String) object.getField("statusId"));
    		twitter.destroyStatus(statusId);
    	}
    	catch (TwitterException e){
    		e.printStackTrace();
    	}
    }
    
    private Twitter getTwitter(ServiceCredential credential){
    	String twitterID = credential.key;
        String twitterPassword = credential.secret;
        System.out.println(twitterID + twitterPassword);

        Twitter twitter = new TwitterFactory().getInstance(twitterID,twitterPassword);
        return twitter;
    }
    
	public enum Property{
		STATUS,
		BLOG 
	}
	
	public static void main(String[] args){
		MObject object = new MObject();
		object.putField("status", "OMG BETA TONIGHT!");
		ServiceCredential mashbot = new ServiceCredential();
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
	

