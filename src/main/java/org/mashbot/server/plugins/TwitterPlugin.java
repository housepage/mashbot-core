package org.mashbot.server.plugins;

import java.util.List;
import java.util.Map;

import twitter4j.*;

import org.mashbot.server.types.MObject;
import org.mashbot.server.types.ServiceCredential;

public class TwitterPlugin extends Plugin {
    private static final String serviceName = "twitter";

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
        return null;
    }

    private void postStatus(MObject object, ServiceCredential credential){
        String twitterID = (String) credential.getField("username");
        String twitterPassword = (String) credential.getField("password");
        String latestStatus = (String)object.getField("status");
        System.out.println(twitterID + twitterPassword);

        Twitter twitter = new TwitterFactory().getInstance(twitterID,twitterPassword);
        Status status;
		try {
			status = twitter.updateStatus(latestStatus);
			System.out.println("Successfully updated the status to [" + status.getText() + "].");
		} catch (TwitterException e) {
			e.printStackTrace();
		}
        
    }
    
	public enum Property{
		STATUS,
		BLOG 
	}
	
	public static void main(String[] args){
		MObject object = new MObject();
		object.putField("status", "OMG BETA TONIGHT!");
		ServiceCredential mashbot = new ServiceCredential();
		mashbot.putField("username", "MashBot");
		mashbot.putField("password", "w1sLm2");
		
		TwitterPlugin plugin = new TwitterPlugin();
		//plugin.setFactory(new TwitterFactory());
		plugin.run("push", "status", object, mashbot);
	}
	
	TwitterFactory factory;

	public TwitterFactory getFactory() {
		return factory;
	}

	public void setFactory(TwitterFactory factory) {
		this.factory = factory;
	}
}
	

