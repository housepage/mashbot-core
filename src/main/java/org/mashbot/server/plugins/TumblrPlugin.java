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

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;

public class TumblrPlugin extends Plugin {
	private static final String serviceName = "tumblr";
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
		
		if(contentType.equals("blog")){
			if(operation.equals("push")){
				content = pushBlog(content, credential);
			}
			if(operation.equals("pull")){
				content = pullBlog(content, credential);
			}
			if(operation.equals("edit")){
				content = editBlog(content, credential);
			}
			if(operation.equals("delete")){
				content = deleteBlog(content, credential);
			}
		}
		
		return content; 
	}
	
	private MObject pushBlog(MObject content, ServiceCredential credential){
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
		return content;
	}
	
	private MObject pullBlog(MObject content, ServiceCredential credential){
		
		return content;
	}
	
	private MObject editBlog(MObject content, ServiceCredential credential){
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
	    	String postId = ((ElementNSImpl) content.getField("BLOG.POSTID")).getTextContent();
			tumblr.edit(email, password, "standard", postTitle, body, tags, new Date().toString(), postId);
			content.putField(MObject.Field.SUCCESS,getServiceName(),true);
		} catch (Exception e) {
			content.putField(MObject.Field.SUCCESS,getServiceName(),false);
		}
		return content;
	}

	private MObject deleteBlog(MObject content, ServiceCredential credential){
		try {
			TumbleJ tumblr = new TumbleJ();
			String email = credential.key;
	    	String password = credential.secret;
	    	String postId = ((ElementNSImpl) content.getField("BLOG.POSTID")).getTextContent();
			tumblr.delete(email, password, postId);
			content.putField(MObject.Field.SUCCESS,getServiceName(),true);
		} catch (Exception e) {
			content.putField(MObject.Field.SUCCESS,getServiceName(),false);
		}
		return content;

	}

	

}
