package org.mashbot.server.plugins;

import java.util.List;
import java.util.Map;

import org.mashbot.server.exceptions.IncompleteInformationException;
import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.ServiceCredential;
import org.mashbot.server.web.MashbotService.Operation;

public abstract class Plugin {
	public abstract MObject run(String operation, String contentType, MObject content, ServiceCredential credential) throws MashbotException;
	public abstract boolean hasRequiredInformation(String operation, String contentType, MObject content);
	public abstract List<String> getRequiredInformation(String operation, String contentType);
	public abstract String getServiceName();
		
	//Map is ContentType->Operation
	public abstract Map<String, List<String>> getSupported();
	
	public enum Property{
		STATUS,
		BLOG 
	}
}