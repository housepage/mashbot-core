package org.mashbot.server.plugins;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mashbot.server.exceptions.IncompleteSecretInformationException;
import org.mashbot.server.exceptions.InvalidConfigFileException;
import org.mashbot.server.exceptions.InvalidFieldException;
import org.mashbot.server.exceptions.InvalidRequestException;
import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.exceptions.MissingAuthenticationException;
import org.mashbot.server.exceptions.UndownloadableContentException;
import org.mashbot.server.plugins.Plugin;
import org.mashbot.server.plugins.TumbleJ;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.ServiceCredential;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.common.collect.Lists;

public class TumblrPlugin extends Plugin {
	private static final String serviceName = "tumblr";
    private Log log = LogFactory.getLog(getClass());	
	private static final Map<String, List<String>> supported;
	private static final List<String> required;
	
    static {
        Map<String, List<String>> aSupported = new HashMap<String,List<String>>();
        List<String> photos = new ArrayList<String>();
		photos.add("push");
		photos.add("pull");
		photos.add("edit");
		photos.add("delete");
		aSupported.put("photo", Collections.unmodifiableList(photos));
        supported = Collections.unmodifiableMap(aSupported);
        
        List<String> aRequired = new ArrayList<String>();
        aRequired.add(MObject.Field.TITLE.toString());
        aRequired.add(MObject.Field.BODY.toString());
        required = Collections.unmodifiableList(aRequired);
    }
	
	public TumblrPlugin(){
		
	}

    public List<String> getRequiredInformation(String operation, String contentType){
        return TumblrPlugin.required;
    }

    public String getServiceName(){
        return serviceName;
    }

    public Map<String, List<String>> getSupported(){
        return TumblrPlugin.supported;
    }

	@Override
	public MObject run(String operation, String contentType, MObject content,
			ServiceCredential credential) throws MashbotException {
		
		log.warn("damn");
		if(contentType.equals("post")){
			log.warn("WHOA");
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
			log.warn("I'm here");
			TumbleJ tumblr = new TumbleJ();
			String email = credential.key;
	    	String password = credential.secret;
	    	String body = content.getStringField(MObject.Field.BODY);
	    	String postTitle = content.getStringField(MObject.Field.TITLE);
	    	log.warn(postTitle);
	    	List<String> tags = content.getField(MObject.Field.TAGS);
	    	StringBuffer tagList = new StringBuffer();
	    	for(String tag : tags){
	    		tagList.append(tag + ",");
	    	}
	    	String stringtags = tagList.toString();
	    	if(stringtags.length() != 0){
	    		stringtags = stringtags.substring(0,stringtags.length()-1);
	    	}
	    	
	    	tumblr.post(email, password, "standard", postTitle, body, stringtags, new Date().toString());
			content.putField(MObject.Field.SUCCESS,new ArrayList<String>(Arrays.asList(new String[] {"true"})), getServiceName());
		} catch (Exception e) {
			e.printStackTrace();
			content.putField(MObject.Field.SUCCESS,new ArrayList<String>(Arrays.asList(new String[] {"false"})), getServiceName());
		}
		return content;
	}
	
	private MObject pullBlog(MObject content, ServiceCredential credential){
		try{
			TumbleJ tumblr = new TumbleJ();
			String email = credential.key;
			String password = credential.secret;
			String tumblrName = content.getField("BLOG.TUMBLRNAME").get(0);
	    	String postId = content.getField(MObject.Field.ID).get(0);
	    	String post = tumblr.pull(email, password, tumblrName, postId);
	    	
	    	//DO XML Stuff
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder documentBuilder = factory.newDocumentBuilder();
	    	Document doc = documentBuilder.parse(new InputSource(new StringReader(post)));

	    	Node blogPost = doc.getElementsByTagName("post").item(0);
	    	NodeList nodes = blogPost.getChildNodes();
	    	for(int i = 0; i < nodes.getLength(); i++){
	    		Node node = nodes.item(i);
	    		String nodeName = node.getNodeName();
	    		if (nodeName.equals("regular-title")){
	    			content.putField(MObject.Field.TITLE, node.getTextContent());
	    		}
	    		if (nodeName.equals("regular-body")){
	    			content.putField(MObject.Field.BODY, node.getTextContent());
	    		}
	    		if (nodeName.equals("tag")){
	    			if(!content.containsField(MObject.Field.TAGS)){
	    				content.putField(MObject.Field.TAGS, node.getTextContent());
	    			}
	    			else{
	    				content.appendField(MObject.Field.TAGS, node.getTextContent());
	    			}
	    		}
	    	}
	    	
			content.putField(MObject.Field.SUCCESS,new ArrayList<String>(Arrays.asList(new String[] {"true"})), getServiceName());
			content.putField(MObject.Field.BODY, post);
		} catch (Exception e) {
			content.putField(MObject.Field.SUCCESS,new ArrayList<String>(Arrays.asList(new String[] {"false"})), getServiceName());
		}
		
		return content;
	}
	
	private MObject editBlog(MObject content, ServiceCredential credential){
		try {
			TumbleJ tumblr = new TumbleJ();
			String email = credential.key;
	    	String password = credential.secret;
	    	log.warn(content.getField(MObject.Field.BODY));
	    	log.warn(content.getField(MObject.Field.TITLE));
	    	log.warn(content.getField(MObject.Field.TAGS));
	    	String body = content.getField(MObject.Field.BODY).get(0);
	    	String postTitle = content.getField(MObject.Field.TITLE).get(0);
	    	String tags = content.getField(MObject.Field.TAGS).get(0);
	    	String postId = content.getField(MObject.Field.ID).get(0);
			tumblr.edit(email, password, "standard", postTitle, body, tags, new Date().toString(), postId);
			content.putField(MObject.Field.SUCCESS,new ArrayList<String>(Arrays.asList(new String[] {"true"})), getServiceName());
		} catch (Exception e) {
			content.putField(MObject.Field.SUCCESS,new ArrayList<String>(Arrays.asList(new String[] {"false"})), getServiceName());
		}
		return content;
	}

	private MObject deleteBlog(MObject content, ServiceCredential credential){
		try {
			TumbleJ tumblr = new TumbleJ();
			String email = credential.key;
	    	String password = credential.secret;
	    	String postId =  content.getField(MObject.Field.ID).get(0);
			tumblr.delete(email, password, postId);
			content.putField(MObject.Field.SUCCESS,new ArrayList<String>(Arrays.asList(new String[] {"true"})), getServiceName());
		} catch (Exception e) {
			content.putField(MObject.Field.SUCCESS,new ArrayList<String>(Arrays.asList(new String[] {"false"})), getServiceName());
		}
		return content;

	}

	public static void main(String[] args) {
		ServiceCredential credential = new ServiceCredential();
		credential.key = "test@mashbot.net";
		credential.secret = "m45hb07";
		
		TumblrPlugin plugin = new TumblrPlugin();
		MObject content = new MObject();
		/* content.putField(MObject.Field.BODY, new ArrayList<String>(Arrays.asList(new String[] {"Mashbot is even more awesome than I previously believed. I love Mashbot!" })));
		content.putField(MObject.Field.TITLE, new ArrayList<String>(Arrays.asList(new String[] {"Mashbot Love (Remix)"})));
		content.putField(MObject.Field.TAGS, new ArrayList<String>(Arrays.asList(new String[] {"Mashbot, Love"}))); */
		content.putField(MObject.Field.ID, new ArrayList<String>(Arrays.asList(new String[] {"582536752"})));
		content.putField("BLOG.TUMBLRNAME", "mashbot");
		try {
			plugin.run("pull", "blog", content, credential);
		} catch (MashbotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected MObject delete(String contentType, MObject content)
			throws MashbotException, IncompleteSecretInformationException,
			InvalidConfigFileException, InvalidFieldException,
			UndownloadableContentException, InvalidRequestException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected MObject edit(String contentType, MObject content)
			throws MashbotException, IncompleteSecretInformationException,
			InvalidConfigFileException, InvalidFieldException,
			UndownloadableContentException, InvalidRequestException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected MObject pull(String contentType, MObject content)
			throws MashbotException, IncompleteSecretInformationException,
			InvalidConfigFileException, InvalidFieldException,
			UndownloadableContentException, InvalidRequestException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected MObject push(String contentType, MObject content)
			throws MashbotException, IncompleteSecretInformationException,
			InvalidConfigFileException, InvalidFieldException,
			UndownloadableContentException, InvalidRequestException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setup(ServiceCredential credential, String operation)
			throws IncompleteSecretInformationException,
			InvalidConfigFileException, MissingAuthenticationException {
		// TODO Auto-generated method stub
		
	}

}
