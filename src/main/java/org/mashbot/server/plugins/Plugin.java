package org.mashbot.server.plugins;

import java.util.List;
import java.util.Map;


import org.mashbot.server.types.MObject;
import org.mashbot.server.types.ServiceCredential;


public abstract class Plugin {
	public abstract MObject run(String operation, String contentType, MObject content, ServiceCredential credential) throws Exception;
	public boolean hasRequiredInformation(String operation, String contentType, MObject content){
		for(String field : getRequiredInformation(operation, contentType)){
			if(!content.containsField(field))
				return false;			
		}
		return true;
	}
	public abstract List<String> getRequiredInformation(String operation, String contentType);
	public abstract String getServiceName();
		
	//Map is ContentType->Operation
	public abstract Map<String, List<String>> getSupported();
	
	public enum Property{
		STATUS,
		BLOG 
	}
}
