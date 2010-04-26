package org.mashbot.server.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.unto.twitter.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.ServiceCredential;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

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
    	String twitterID = credential.key;
    	String twitterPassword = credential.secret;
    	String latestStatus = ((ElementNSImpl) object.getField("STATUS")).getTextContent();
        
    	Twitter twitter = new TwitterFactory().getInstance(twitterID, twitterPassword);
    	try {
			twitter.updateStatus(latestStatus);
		} catch (TwitterException e) {
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
}
	

