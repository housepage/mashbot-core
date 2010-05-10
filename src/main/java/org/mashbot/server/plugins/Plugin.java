package org.mashbot.server.plugins;

import java.util.List;
import java.util.Map;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mashbot.server.exceptions.IncompleteSecretInformationException;
import org.mashbot.server.exceptions.InvalidConfigFileException;
import org.mashbot.server.exceptions.InvalidFieldException;
import org.mashbot.server.exceptions.InvalidRequestException;
import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.exceptions.MissingAuthenticationException;
import org.mashbot.server.exceptions.UndownloadableContentException;
import org.mashbot.server.types.MObject;
import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.types.ServiceCredential;

import twitter4j.TwitterException;

public abstract class Plugin {
	private Log log = LogFactory.getLog(getClass());

	public MObject run(String operation, String contentType, MObject content, ServiceCredential credential) throws MashbotException {
		this.setup(credential,operation);
		
		if(operation.equals("push")){
			return this.push(contentType, content);
		} else if(operation.equals("pull")) {
			return this.pull(contentType, content);
		} else if(operation.equals("edit")) {
			return this.edit(contentType,content);
		} else if(operation.equals("delete")) {
			return this.delete(contentType,content);
		}
		
		return content;
	}
	
	protected abstract MObject push(String contentType, MObject content) throws MashbotException, IncompleteSecretInformationException, InvalidConfigFileException, InvalidFieldException, UndownloadableContentException, InvalidRequestException;
	protected abstract MObject pull(String contentType, MObject content) throws MashbotException, IncompleteSecretInformationException, InvalidConfigFileException, InvalidFieldException, UndownloadableContentException, InvalidRequestException;
	protected abstract MObject edit(String contentType, MObject content) throws MashbotException, IncompleteSecretInformationException, InvalidConfigFileException, InvalidFieldException, UndownloadableContentException, InvalidRequestException;
	protected abstract MObject delete(String contentType, MObject content) throws MashbotException, IncompleteSecretInformationException, InvalidConfigFileException, InvalidFieldException, UndownloadableContentException, InvalidRequestException;
	protected abstract void setup(ServiceCredential credential, String operation) throws IncompleteSecretInformationException, InvalidConfigFileException, MissingAuthenticationException;
	
	public boolean hasRequiredInformation(String operation, String contentType, MObject content){
		for(String field : getRequiredInformation(operation, contentType)){
			log.warn("field: "+field+" "+content.getFields());
			if(!content.containsField(field))
				return false;			
		}
		return true;
	}
	public abstract List<String> getRequiredInformation(String operation, String contentType);
	
	public abstract String getServiceName();
		
	//Map is ContentType->Operation
	public abstract Map<String, List<String>> getSupported();
	
	protected String serviceName;
	
	public enum Property{
		STATUS,
		BLOG 
	}
}
