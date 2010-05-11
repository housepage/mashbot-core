package org.mashbot.server.plugins;

import java.io.StringReader;
import java.io.IOException;

import java.net.URL;
import java.net.MalformedURLException;

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

import com.google.gdata.client.Query;
import com.google.gdata.client.blogger.BloggerService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.Entry;
import com.google.gdata.data.Feed;
import com.google.gdata.data.Person;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.TextContent;
import com.google.gdata.util.ServiceException;

import com.google.gdata.client.authn.oauth.GoogleOAuthHelper;
import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.client.authn.oauth.OAuthSigner;
import com.google.gdata.client.authn.oauth.OAuthException;

import org.mashbot.server.exceptions.IncompleteSecretInformationException;
import org.mashbot.server.exceptions.InvalidConfigFileException;
import org.mashbot.server.exceptions.InvalidFieldException;
import org.mashbot.server.exceptions.InvalidRequestException;
import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.exceptions.MissingAuthenticationException;
import org.mashbot.server.exceptions.UndownloadableContentException;

import org.mashbot.server.plugins.Plugin;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.ServiceCredential;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;

public class BloggerPlugin extends Plugin
{
  private static final String applicationName = "mashbot";
  private static final String serviceName = "blogger";
  private Log log = LogFactory.getLog(getClass());

  private Map<String, List<String>> supported;


  
  private static final String METAFEED_URL = 
      "http://www.blogger.com/feeds/default/blogs";

  private static final String FEED_URI_BASE = "http://www.blogger.com/feeds";
  private static final String POSTS_FEED_URI_SUFFIX = "/posts/default";
  private static final String COMMENTS_FEED_URI_SUFFIX = "/comments/default";

  private String feedUri;
  private BloggerService blogger; 

	public BloggerPlugin(){
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

  private static String getBlogId(BloggerService myService)
      throws ServiceException, IOException {
    // Get the metafeed
    final URL feedUrl = new URL(METAFEED_URL);
    Feed resultFeed = myService.getFeed(feedUrl, Feed.class);

    // If the user has a blog then return the id (which comes after 'blog-')
    if (resultFeed.getEntries().size() > 0) {
      Entry entry = resultFeed.getEntries().get(0);
      return entry.getId().split("blog-")[1];
    }
    throw new IOException("User has no blogs!");
  }

  protected void setup(ServiceCredential credential, String operation) 
    throws IncompleteSecretInformationException, InvalidConfigFileException, 
                  MissingAuthenticationException
  {
    /* Translate credentials into OAuth stuff */
    GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();

    oauthParameters.setOAuthConsumerKey(credential.key);
    oauthParameters.setOAuthConsumerSecret(credential.secret);

    OAuthSigner signer = new OAuthHmacSha1Signer();

    GoogleOAuthHelper oauthHelper = new GoogleOAuthHelper(signer);

    oauthParameters.setScope(FEED_URI_BASE);

    /* Login to the service with the OAuth and assign the blog id */
    blogger = new BloggerService(applicationName);

    try {
      blogger.setOAuthCredentials(oauthParameters, signer);
    }
    catch(OAuthException e)
    {
      log.error("OAuth problem with Blogger");
      throw new MissingAuthenticationException();
    }

    String blogId;
    try
    {
      blogId = getBlogId(blogger);
    }
    catch(IOException e)
    {
      throw new MissingAuthenticationException();
    }
    catch(ServiceException e)
    {
      throw new MissingAuthenticationException();
    }
    feedUri = FEED_URI_BASE + "/" + blogId;
  }
	
	protected MObject push(String contentType, MObject content) 
    throws MashbotException, IncompleteSecretInformationException, 
      InvalidConfigFileException, InvalidFieldException, 
      UndownloadableContentException, InvalidRequestException
  {
    if(!contentType.equals("blog"))
    {
      log.error("Content type not equal blog");
			content.putField(MObject.Field.SUCCESS,
          new ArrayList<String>(Arrays.asList(new String[] {"false"})), 
            getServiceName());

      return content;
    }

    log.warn(content.getField("BLOG.BODY"));
    log.warn(content.getField("BLOG.TITLE"));
    log.warn(content.getField("BLOG.TAGS"));

    /* Extract all the goodies */
    String body = content.getField("BLOG.BODY").get(0);
    String title = content.getField("BLOG.TITLE").get(0);
    String tags = content.getField("BLOG.TAGS").get(0);

    Entry entry = new Entry();
    entry.setTitle(new PlainTextConstruct(title));
    entry.setContent(new PlainTextConstruct(body)); 

    try
    {
      URL postUrl = new URL(feedUri + POSTS_FEED_URI_SUFFIX);
      blogger.insert(postUrl, entry);
    }
    catch(MalformedURLException e)
    {
      /* be confused */
    }
    catch(ServiceException e)
    {
      throw new MashbotException();
    }
    catch(IOException e)
    {
      log.error("IO exception");
      content.putField(MObject.Field.SUCCESS,
          new ArrayList<String>(Arrays.asList(new String[] {"false"})), 
            getServiceName());
    }

    content.putField(MObject.Field.SUCCESS,
        new ArrayList<String>(Arrays.asList(new String[] {"true"})), 
          getServiceName());
  /*
    catch(Exception e) {
			content.putField(MObject.Field.SUCCESS,
          new ArrayList<String>(Arrays.asList(new String[] {"false"})), 
            getServiceName());
		}
    */
		return content;
	}
	
	protected MObject pull(String contentType, MObject content) 
    throws MashbotException, IncompleteSecretInformationException, 
      InvalidConfigFileException, InvalidFieldException, 
      UndownloadableContentException, InvalidRequestException
  {
    /*
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
    */
		
		return content;
	}
	
	protected MObject edit(String contentType, MObject content) 
    throws MashbotException, IncompleteSecretInformationException, 
      InvalidConfigFileException, InvalidFieldException, 
      UndownloadableContentException, InvalidRequestException
  {
    /*
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
    */
		return content;
	}

	protected MObject delete(String contentType, MObject content) 
    throws MashbotException, IncompleteSecretInformationException, 
      InvalidConfigFileException, InvalidFieldException, 
      UndownloadableContentException, InvalidRequestException
  {
    /*
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
    */
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
		} catch (MashbotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}

