package org.mashbot.server.plugins;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.mashbot.server.exceptions.IncompleteInformationException;
import org.mashbot.server.exceptions.MissingAuthenticationException;
import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.ServiceCredential;
import org.springframework.core.io.UrlResource;
import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.Permission;
import com.aetrion.flickr.uploader.UploadMetaData;
import com.aetrion.flickr.uploader.Uploader;

public class FlickrPlugin extends Plugin {
	
	private static final int DEFAULTTRIES = 3;
	Auth auth;
	Flickr flickr;
	Uploader uploader;

	@Override
	public List<String> getRequiredInformation(String operation,
			String contentType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServiceName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<String>> getSupported() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasRequiredInformation(String operation, String contentType,
			MObject content) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public MObject run(String operation, String contentType, MObject content,
		ServiceCredential credential) throws IncompleteSecretInformationException, InvalidConfigFileException {
		
		this.setup(credential,operation);

		Flickr flickr = getFlickr();
		Auth auth = new Auth();
		auth.setToken(credential.secret);
		
		if(contentType == "picture"){
			if(operation == "push"){
				return this.push(content);
			} else if(operation == "pull") {
				return this.push(content);
			} else if(operation == "edit") {
				return this.edit(content);
			} else if(operation == "delete") {
				return this.delete(content);
			}
		}
		return content;
	}
	
	private MObject delete(MObject content) {
		// TODO Auto-generated method stub
		return null;
	}

	private MObject edit(MObject content) {
		// TODO Auto-generated method stub
		return null;
	}
	

	private MObject push(MObject content){
		return push(content,DEFAULTTRIES);
	}

	private MObject push(MObject content, int tries) throws InvalidFieldException, IncompleteSecretInformationException, InvalidConfigFileException, UndownloadableContentException, InvalidRequestException {
		
		if(tries <= 0){
			return content;
		}
		
		Flickr flickr = getFlickr();
		
		String picture = content.getStringField(MObject.Field.URL);
		URL url;
		try {
			url = new URL(picture);
		} catch (MalformedURLException e) {
			throw new InvalidFieldException(MObject.Field.URL);
		}
		InputStream in;
		try {
			in = url.openStream();
		} catch (IOException e) {
			throw new UndownloadableContentException(); 
		}
		
		UploadMetaData metaData = new UploadMetaData();
		
		String title = content.getStringField(MObject.Field.URL);
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
		
		try {
			String id = this.uploader.upload(in, metaData);
			content.putField(MObject.Field.ID, id, getServiceName());
		} catch (IOException e) {
			return this.push(content, tries-1);
		} catch (FlickrException e) {
			throw new InvalidRequestException(e);
		} catch (SAXException e) {
			throw new InvalidFieldException(e.getMessage());
		}
	}

	private void setup(ServiceCredential credential, String operation) throws IncompleteSecretInformationException, InvalidConfigFileException {
			this.flickr = getFlickr();
			
			if(credential.method == "proprietary" && credential.secret.length() > 0){
				this.auth = new Auth();
				this.auth.setToken(credential.secret);
			} else {
				this.auth.setPermission(Permission.NONE);
			}
			
			if(operation == "push"){
				this.auth.setPermission(Permission.WRITE);
			} else if(operation == "pull") {
				this.auth.setPermission(Permission.READ);
			} else if(operation == "edit") {
				this.auth.setPermission(Permission.WRITE);
			} else if(operation == "delete") {
				this.auth.setPermission(Permission.DELETE);
			}
			
			this.uploader = this.flickr.getUploader();
				
	}


	private Flickr getFlickr() throws IncompleteSecretInformationException, InvalidConfigFileException {

		Properties properties = new Properties();
		
		try{

		FileInputStream inFile;
		try {
			inFile = new FileInputStream("/src/main/resources/FlickrPlugin.config");
			properties.load(inFile);
		} catch (IOException e) {
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
}
