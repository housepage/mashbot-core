package org.mashbot.server.plugins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mashbot.server.exceptions.IncompleteSecretInformationException;
import org.mashbot.server.exceptions.InvalidConfigFileException;
import org.mashbot.server.exceptions.InvalidFieldException;
import org.mashbot.server.exceptions.InvalidRequestException;
import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.exceptions.MissingAuthenticationException;
import org.mashbot.server.exceptions.UndownloadableContentException;

import org.mashbot.server.types.GenericFieldStorage;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.MashbotQuery;
import org.mashbot.server.types.ServiceCredential;

import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;

public class TwitterPlugin extends Plugin {
    private static final String serviceName = "twitter";
    private static final Map<String, List<String>> supported;
    private Twitter twitter;
    
    static {
        Map<String, List<String>> aSupported = new HashMap<String,List<String>>();
        List<String> photos = new ArrayList<String>();
		photos.add("push");
		photos.add("pull");
		photos.add("edit");
		photos.add("delete");
		aSupported.put("status", Collections.unmodifiableList(photos));
        supported = Collections.unmodifiableMap(aSupported);
    }

    public List<String> getRequiredInformation(String operation, String contentType){
    	List<String> requiredInformation = new ArrayList<String>();
    	
    	if(contentType.equals("status")){
    		if (operation.equals("push")){
    			requiredInformation.add(MObject.Field.STATUS.toString());
    		}
    		if (operation.equals("pull")){
    			requiredInformation.add(MObject.Field.ID.toString());
    		}
    		if (operation.equals("delete")){
    			requiredInformation.add(MObject.Field.ID.toString());
    		}
    	}
    	return requiredInformation;
    }

    public String getServiceName(){
        return serviceName;
    }

    public Map<String, List<String>> getSupported(){
        Map<String, List<String>> supported = new HashMap<String, List<String>>();
        List<String> operations = Arrays.asList(new String[] {"push", "pull", "delete"});
        supported.put("status", operations);
        return supported;
    }

    private void postStatus(MObject object, ServiceCredential credential){
    	String latestStatus = object.getStringField("STATUS");
    	
    	Status status;

		try {
			status = twitter.updateStatus(latestStatus);
			System.out.println("Successfully updated the status to [" + status.getText() + "].");
		} catch (TwitterException e) {
			e.printStackTrace();
		}     
    }
    
    private Twitter getTwitter(ServiceCredential credential) throws MissingAuthenticationException{
    	
    	if(credential.method.equals("userpass")){
    		return new TwitterFactory().getInstance(credential.key, credential.secret);
    	} else if(credential.method.equals("oauth")) {
    		AccessToken access = new AccessToken(credential.key,credential.secret);
    		return new TwitterFactory().getOAuthAuthorizedInstance("wOrsK4bFG0VJNplruzVoNg","E3akoP3oG8iQj1XHP9bArQSwlCKfyedPNaFX4U8vd4", access);
    	} else {
    		throw new MissingAuthenticationException();
    	}
    	
    }
    
	public enum Property{
		STATUS,
		BLOG 
	}

	public static void main(String[] args){
		MObject object = new MObject();
		object.putField("status", "OMG BETA TONIGHT!");
		ServiceCredential mashbot = new ServiceCredential();
		mashbot.key = "MashBot";
		mashbot.secret = "w1sLm2";
		
		TwitterPlugin plugin = new TwitterPlugin();
		//plugin.setFactory(new TwitterFactory());
		try {
			plugin.run("push", "status", object, mashbot);
		} catch (InvalidFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MashbotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean hasRequiredInformation(String operation, String contentType,
			MObject content) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	protected MObject delete(String contentType, MObject content)
			throws MashbotException, IncompleteSecretInformationException,
			InvalidConfigFileException, InvalidFieldException,
			UndownloadableContentException, InvalidRequestException {
		
		long statusId = Long.parseLong((String) content.getStringField(MObject.Field.ID,serviceName));
		
		try {
    		Status result = twitter.destroyStatus(statusId);
    		content.putField(MObject.Field.SUCCESS,MObject.Field.TRUE.toString(), serviceName,result.getUser().getName(), statusId);
    	}
    	catch (TwitterException e){
    		content.putField(MObject.Field.SUCCESS,MObject.Field.FALSE.toString(), serviceName,"unknown", statusId);
    	}
    	
    	return content;
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

    	for(MashbotQuery i : content.getQueries()){
    		
    	}
    	
    	try {
    		long statusId = Long.parseLong((String) content.getStringField(MObject.Field.ID));
    		Status status = this.twitter.showStatus(statusId);
    		
    		String statusText = status.getText();
    		String user = status.getUser().getName();
    		content.putField(MObject.Field.STATUS, statusText, serviceName, user);
    		content.putField("user", user);
    	}
    	catch (TwitterException e) {
    		e.printStackTrace();
    	}
    	
    	return content;
	}

	@Override
	protected MObject push(String contentType, MObject content)
			throws MashbotException, IncompleteSecretInformationException,
			InvalidConfigFileException, InvalidFieldException,
			UndownloadableContentException, InvalidRequestException {
		String latestStatus = content.getStringField(MObject.Field.STATUS);
    	
    	Status status;

		try {
			status = twitter.updateStatus(latestStatus);
			content.putField(MObject.Field.SUCCESS,MObject.Field.FALSE.toString(), serviceName,status.getUser().getName(),status.getId());
		} catch (TwitterException e) {
			content.putField(MObject.Field.ID,MObject.Field.FALSE.toString());
		}
		
		return content;
	}

	@Override
	protected void setup(ServiceCredential credential, String operation)
			throws IncompleteSecretInformationException,
			InvalidConfigFileException, MissingAuthenticationException {
		this.twitter = getTwitter(credential);
		
	}
	
}
	

