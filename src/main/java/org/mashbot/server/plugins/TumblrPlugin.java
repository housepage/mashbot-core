package org.mashbot.server.plugins;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.plugins.Plugin;
import org.mashbot.server.plugins.TumbleJ;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.ServiceCredential;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

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
	    	String body = content.getField("BLOG.BODY").get(0);
	    	String postTitle = content.getField("BLOG.TITLE").get(0);
	    	String tags = content.getField("BLOG.TAGS").get(0);
			tumblr.post(email, password, "standard", postTitle, body, tags, new Date().toString());
			content.putField(MObject.Field.SUCCESS,new ArrayList<String>(Arrays.asList(new String[] {"true"})), getServiceName());
		} catch (Exception e) {
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
	    	String postId = content.getField("BLOG.POSTID").get(0);
	    	String post = tumblr.pull(email, password, tumblrName, postId);
	    	
	    	//DO XML Stuff
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder documentBuilder = factory.newDocumentBuilder();
	    	Document doc = documentBuilder.parse(new InputSource(new StringReader(post)));
<<<<<<< HEAD
	    	Node blogPost = doc.getElementsByTagName("post").item(0);
	    	NodeList nodes = blogPost.getChildNodes();
	    	for(int i = 0; i < nodes.getLength(); i++){
	    		Node node = nodes.item(i);
	    		String nodeName = node.getNodeName();
	    		if (nodeName.equals("regular-title")){
	    			content.putField("BLOG.TITLE", node.getTextContent());
	    		}
	    		if (nodeName.equals("regular-body")){
	    			content.putField("BLOG.BODY", node.getTextContent());
	    		}
	    		if (nodeName.equals("tag")){
	    			if(!content.containsKey("BLOG.TAGS")){
	    				content.putField("BLOG.TAGS", node.getTextContent());
	    			}
	    			else{
	    				content.appendField("BLOG.TAGS", node.getTextContent());
	    			}
	    		}
	    	}
	    	
			content.putField(MObject.Field.SUCCESS,new ArrayList<String>(Arrays.asList(new String[] {"true"})), getServiceName());
			content.putField("BLOG.BODY", post);
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
	    	log.warn(content.getField("BLOG.BODY"));
	    	log.warn(content.getField("BLOG.TITLE"));
	    	log.warn(content.getField("BLOG.TAGS"));
	    	String body = content.getField("BLOG.BODY").get(0);
	    	String postTitle = content.getField("BLOG.TITLE").get(0);
	    	String tags = content.getField("BLOG.TAGS").get(0);
	    	String postId = content.getField("BLOG.POSTID").get(0);
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
	    	String postId =  content.getField("BLOG.POSTID").get(0);
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
		/* content.putField("BLOG.BODY", new ArrayList<String>(Arrays.asList(new String[] {"Mashbot is even more awesome than I previously believed. I love Mashbot!" })));
		content.putField("BLOG.TITLE", new ArrayList<String>(Arrays.asList(new String[] {"Mashbot Love (Remix)"})));
		content.putField("BLOG.TAGS", new ArrayList<String>(Arrays.asList(new String[] {"Mashbot, Love"}))); */
		content.putField("BLOG.POSTID", new ArrayList<String>(Arrays.asList(new String[] {"582536752"})));
		content.putField("BLOG.TUMBLRNAME", "mashbot");
		try {
			plugin.run("pull", "blog", content, credential);
			System.out.println(content.getField("BLOG.BODY").get(0));
		} catch (MashbotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
