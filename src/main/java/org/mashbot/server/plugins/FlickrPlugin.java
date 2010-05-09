package org.mashbot.server.plugins;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.mashbot.server.types.MObject;
import org.mashbot.server.types.ServiceCredential;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.uploader.UploadMetaData;
import com.aetrion.flickr.uploader.Uploader;

public class FlickrPlugin extends Plugin {

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
			ServiceCredential credential) {
		Flickr flickr = getFlickr();
		Auth auth = new Auth();
		auth.setToken(credential.secret);
		
		return null;
	}
	
	private Flickr getFlickr() throws IOException, ParserConfigurationException, Exception{
		Properties properties = new Properties();

		FileInputStream inFile = new FileInputStream("/src/main/resources/FlickrPlugin.config");
		properties.load(inFile);
		
		if(properties.containsKey("apiKey") && properties.containsKey("secret")){
			String apiKey = properties.getProperty("apiKey");
			String secret = properties.getProperty("secret");
			
			Flickr flickr = new Flickr(apiKey, secret, new REST());
			return flickr;
		}
		else{
			throw new Exception();
		}
		
	}

}
