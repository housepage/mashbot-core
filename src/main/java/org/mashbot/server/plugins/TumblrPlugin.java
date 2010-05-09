package org.mashbot.server.plugins;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.ServiceCredential;

public class TumblrPlugin extends Plugin {
	private static final String serviceName = "twitter";
    private Log log = LogFactory.getLog(getClass());
	private Map<String, List<String>> supported;
	
	public TumblrPlugin(){
		this.supported = new HashMap<String,List<String>>();
		List<String> supportedStatus = new ArrayList<String>();
		supportedStatus.add("push");
		supportedStatus.add("pull");
		supportedStatus.add("edit");
		supportedStatus.add("delete");
		this.supported.put("blog", supportedStatus);
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

	@Override
	public MObject run(String operation, String contentType, MObject content,
			ServiceCredential credential) throws MashbotException {
		
		if(operation == "push" && contentType == "blog"){
			try {
				TumbleJ tumblr = new TumbleJ();
				String email = credential.key;
		    	String password = credential.secret;
		    	log.warn(content.getField("BLOG.BODY"));
		    	log.warn(content.getField("BLOG.TITLE"));
		    	log.warn(content.getField("BLOG.TAGS"));
		    	String body = ((ElementNSImpl) content.getField("BLOG.BODY")).getTextContent();
		    	String postTitle = ((ElementNSImpl) content.getField("BLOG.TITLE")).getTextContent();
		    	String tags = ((ElementNSImpl) content.getField("BLOG.TAGS")).getTextContent();
				tumblr.post(email, password, "standard", postTitle, body, tags, new Date().toString());
				content.putField(MObject.Field.SUCCESS,getServiceName(),true);
			} catch (Exception e) {
				content.putField(MObject.Field.SUCCESS,getServiceName(),false);
			}
		}
		return content; 
	}

}
