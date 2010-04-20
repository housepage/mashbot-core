package org.mashbot.server.plugins;

import java.util.List;
import java.util.Map;

import twitter4j.*;

import org.mashbot.server.types.MObject;
import org.mashbot.server.types.MObject.Field;

public class TwitterPlugin extends Plugin {
    private static final String serviceName = "twitter";

	public MObject run(String operation, String contentType, MObject content) {
        if ( operation == "push" && contentType == "status"){
            postStatus(content);
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
        return "Twitter Plugin";
    }

    public Map<String, List<String>> getSupported(){
        return null;
    }

    private void postStatus(MObject object){
        String twitterID = (String) object.getField(MObject.Field.USERNAME+"."+serviceName);
        String twitterPassword = (String) object.getField(MObject.Field.PASSWORD+"."+serviceName);
        String latestStatus = (String)object.getField("status");
        System.out.println(twitterID + twitterPassword);

        Twitter twitter = factory.getInstance(twitterID,twitterPassword);
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
		object.putField("status", "Wonderbar!");
		object.putField(MObject.Field.USERNAME+"."+serviceName, "MashBot");
		object.putField(MObject.Field.PASSWORD+"."+serviceName, "w1sLm2");
		TwitterPlugin plugin = new TwitterPlugin();
		plugin.setFactory(new TwitterFactory());
		plugin.run("push", "status", object);
	}
	
	TwitterFactory factory;

	public TwitterFactory getFactory() {
		return factory;
	}

	public void setFactory(TwitterFactory factory) {
		this.factory = factory;
	}
}
	

