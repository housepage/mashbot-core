package org.mashbot.server.plugins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mashbot.server.exceptions.InvalidFieldException;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.MashbotQuery;
import org.mashbot.server.types.ServiceCredential;

import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterPlugin extends Plugin {
    private static final String serviceName = "twitter";

	public MObject run(String operation, String contentType, MObject content, ServiceCredential credential) throws InvalidFieldException {
		
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
        Map<String, List<String>> supported = new HashMap<String, List<String>>();
        List<String> operations = Arrays.asList(new String[] {"push", "pull", "delete"});
        supported.put("status", operations);
        return supported;
    }

    private void postStatus(MObject object, ServiceCredential credential){
    	String latestStatus = object.getStringField("STATUS");

      Twitter twitter = getTwitter(credential);
    	
      Status status;

			try {
				status = twitter.updateStatus(latestStatus);
				System.out.println("Successfully updated the status to [" + status.getText() + "].");
			} catch (TwitterException e) {
				e.printStackTrace();
			}     
    }
    
    private MObject pullStatus(MObject object, ServiceCredential credential) throws InvalidFieldException {
    	Twitter twitter = getTwitter(credential); 
    	MObject retObject = new MObject();

    	for(MashbotQuery i : object.getQueries()){
    		
    	}
    	
    	try {
    		long statusId = Long.parseLong((String) object.getStringField(MObject.Field.ID));
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
    		long statusId = Long.parseLong((String) object.getStringField("statusId"));
    		twitter.destroyStatus(statusId);
    	}
    	catch (TwitterException e){
    		e.printStackTrace();
    	}
    }
    
    private Twitter getTwitter(ServiceCredential credential){
    		String twitterID = credential.key;
        String twitterPassword = credential.secret;
				
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
		try {
			plugin.run("push", "status", object, mashbot);
		} catch (InvalidFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	TwitterFactory factory;

	public TwitterFactory getFactory() {
		return factory;
	}

	public void setFactory(TwitterFactory factory) {
		this.factory = factory;
	}

	@Override
	public boolean hasRequiredInformation(String operation, String contentType,
			MObject content) {
		// TODO Auto-generated method stub
		return true;
	}
}
	

