package org.mashbot.server.plugins;

import java.util.List;
import twitter4j.*;

import org.mashbot.server.types.MObject;

public class TwitterPlugin extends Plugin {
    public MObject run(String operation, String contentType, MObject content) {
        if ( operation == "post" && contentType == "status"){
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

    public List<String> getSupported(){
        return null;
    }

    private void postStatus(MObject object){
        String twitterID = (String) object.getField("user");
        String twitterPassword = (String) object.getField("password");
        String latestStatus = (String)object.getField("status");

        Twitter twitter = new TwitterFactory().getInstance(twitterID,twitterPassword);
        Status status;
		try {
			status = twitter.updateStatus(latestStatus);
			System.out.println("Successfully updated the status to [" + status.getText() + "].");
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
	public enum Property{
		STATUS,
		BLOG 
	}
	
	public static void main(String[] args){
		MObject object = new MObject("");
		object.putField("status", "Test!");
		object.putField("user", "MashBot");
		object.putField("password", "w1sLm2");
		TwitterPlugin plugin = new TwitterPlugin();
		plugin.run("post", "status", object);
		
	}
}
	

