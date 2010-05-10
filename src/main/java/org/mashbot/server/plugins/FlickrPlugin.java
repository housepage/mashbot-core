package org.mashbot.server.plugins;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mashbot.server.exceptions.IncompleteInformationException;
import org.mashbot.server.exceptions.IncompleteSecretInformationException;
import org.mashbot.server.exceptions.InvalidConfigFileException;
import org.mashbot.server.exceptions.InvalidFieldException;
import org.mashbot.server.exceptions.InvalidRequestException;
import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.exceptions.MissingAuthenticationException;
import org.mashbot.server.exceptions.UndownloadableContentException;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.ServiceCredential;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.Permission;
import com.aetrion.flickr.uploader.UploadMetaData;
import com.aetrion.flickr.uploader.Uploader;

public class FlickrPlugin extends Plugin {
	
	private static final int DEFAULTTRIES = 3;
	Flickr flickr;
	Uploader uploader;
	private static String serviceName = "flickr";
	private Map<String,List<String>> supported ;
	private static Log log = LogFactory.getLog(FlickrPlugin.class);
	
	public FlickrPlugin(){
		this.supported = new HashMap<String,List<String>>();
		List<String> pictures = new ArrayList<String>();
		pictures.add("push");
		pictures.add("pull");
		pictures.add("edit");
		pictures.add("delete");
		this.supported.put("picture", pictures);
	}

	@Override
	public List<String> getRequiredInformation(String operation,
			String contentType) {
		return null;
	}

	@Override
	public String getServiceName() {
		return serviceName ; 
	}

	@Override
	public Map<String, List<String>> getSupported() {
		return this.supported;
	}

	@Override
	public boolean hasRequiredInformation(String operation, String contentType,
			MObject content) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public MObject run(String operation, String contentType, MObject content,
			ServiceCredential credential) throws MashbotException, IncompleteSecretInformationException, InvalidConfigFileException, InvalidFieldException, UndownloadableContentException, InvalidRequestException {
		
		this.setup(credential,operation);
		
		log.warn(operation + " " + contentType + " " + content);
		
		if(contentType.equals("picture")){
			if(operation.equals("push")){
				log.warn("Hooray!");
				return this.push(content);
			} else if(operation.equals("pull")) {
				return this.pull(content);
			} else if(operation.equals("edit")) {
				return this.edit(content);
			} else if(operation.equals("delete")) {
				return this.delete(content);
			}
		}
		return content;
	}
	
	private MObject pull(MObject content) {
		// TODO Auto-generated method stub
		return null;
	}

	private MObject delete(MObject content) {
		// TODO Auto-generated method stub
		return null;
	}

	private MObject edit(MObject content) {
		// TODO Auto-generated method stub
		return null;
	}

	private MObject push(MObject content) throws MashbotException {
		log.warn("Hello");
		Flickr flickr = getFlickr();
		String picture = content.getStringField(MObject.Field.URL);
		log.warn(picture);
		URL url;
		try {
			url = new URL(picture);
		} catch (MalformedURLException e) {
			throw new InvalidFieldException(MObject.Field.URL);
		}
		InputStream in;
		try {
			log.warn("Before: "+url+" "+picture);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(0);
			connection.setReadTimeout(0);
			in = connection.getInputStream();
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new UndownloadableContentException(); 
		}
		
		UploadMetaData metaData = new UploadMetaData();
		
		String title = content.getStringField(MObject.Field.TITLE);
		metaData.setTitle(title);
		
		if(content.containsField(MObject.Field.CAPTION)){
			String caption = content.getStringField(MObject.Field.CAPTION);
			metaData.setDescription(caption);
		}
		
		if(content.containsField(MObject.Field.TAGS)){
			Collection<String> tags = content.getField(MObject.Field.TAGS);
			metaData.setTags(tags);
		}
		
		if(content.containsField(MObject.Field.ALBUM)){
			String caption = content.getStringField(MObject.Field.ALBUM);
			metaData.setDescription(caption);
		}
		
		log.warn("Hello again");
		
		try {
			log.warn(RequestContext.getRequestContext().getAuth().getToken());
			
			String id = this.uploader.upload(in, metaData);
			RequestContext reqcon = RequestContext.getRequestContext();
			Auth current = reqcon.getAuth();
			
			
			content.putField(MObject.Field.ID, id, getServiceName());
		} catch (IOException e) {
			throw new MashbotException();
		} catch (FlickrException e) {
			throw new InvalidRequestException(e);
		} catch (SAXException e) {
			throw new InvalidFieldException(e.getMessage());
		}
		
		return content;
	}

	private void setup(ServiceCredential credential, String operation) throws IncompleteSecretInformationException, InvalidConfigFileException {
			this.flickr = getFlickr();
			
			Auth auth = new Auth();
			
			if(credential.method == "proprietary" && credential.secret.length() > 0){
				auth.setToken(credential.secret);
				
				if(operation == "push"){
					auth.setPermission(Permission.WRITE);
				} else if(operation == "pull") {
					auth.setPermission(Permission.READ);
				} else if(operation == "edit") {
					auth.setPermission(Permission.WRITE);
				} else if(operation == "delete") {
					auth.setPermission(Permission.DELETE);
				}
				
			} else {
				auth.setPermission(Permission.NONE);
			}
			
			
			RequestContext.getRequestContext().setAuth(auth);
			this.uploader = this.flickr.getUploader();
	}


	private Flickr getFlickr() throws IncompleteSecretInformationException, InvalidConfigFileException {
		Properties properties = new Properties();

		FileInputStream inFile;
		try {
			
			inFile = new FileInputStream(new ClassPathResource("src/main/resources/FlickrPlugin.config").getPath());
			properties.load(inFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw new InvalidConfigFileException("/src/main/resources/FlickrPlugin.config");
		}
		
		if(properties.containsKey("apiKey") && properties.containsKey("secret")){
			String apiKey = properties.getProperty("apiKey");
			String secret = properties.getProperty("secret");
			
			Flickr flickr;
			try {
				flickr = new Flickr(apiKey, secret, new REST());
			} catch (ParserConfigurationException e) {
				throw new InvalidConfigFileException("/src/main/resources/FlickrPlugin.config");
			}
			return flickr;
		}
		else{
			throw new IncompleteSecretInformationException();
		}	
	}
	
	public static void main(String [] args){
		FlickrPlugin in = new FlickrPlugin();
		
		MObject hooray = new MObject();
		hooray.putField(MObject.Field.URL, "http://3.bp.blogspot.com/_JEfe6AelcdQ/R37vGY0tBSI/AAAAAAAAAYE/oru4LgbUpGM/s320/_41186601_hooray-pa5.jpg");
		hooray.putField(MObject.Field.TITLE,"Epic Freezer");
		
		ServiceCredential credential = new ServiceCredential();
		credential.key = "NoHotDogBuns";
		credential.secret = "72157623902418777-4836d51734ef7593";
		credential.method = "proprietary";
		
		try {
			MObject herro = in.run("push", "picture", hooray, credential);
			log.warn(herro);
		} catch (IncompleteSecretInformationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UndownloadableContentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MashbotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
