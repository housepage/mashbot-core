package org.mashbot.server.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mashbot.server.exceptions.IncompleteInformationException;
import org.mashbot.server.plugins.Plugin;
import org.mashbot.server.types.Request;
import org.mashbot.server.types.RequestContext;
import org.mashbot.server.types.Response;

/**
 * 
 * @author andrew
 * 
 * This handler verifies that the required information is being sent to the plugins
 *
 */

public class RequiredDataVerificationHandler extends ChainableHandler {

	@Override
	public void preRequest(Request in, Response out, RequestContext context) throws IncompleteInformationException{
		
		String contentType = (String) in.getField(Request.Field.CONTENTTYPE);
		String operation = (String) in.getField(Request.Field.OPERATION);
		List<Plugin> plugins = context.getPlugins();
		
		Map<String, List<String>> requiredFields = new HashMap<String, List<String>>();
		
		for(Plugin plugin : plugins){
			Map <String, List<String>> supported = plugin.getSupported();
			
			if (supported.containsKey(contentType)){
				if(supported.get(contentType).contains(operation)){
					if(!plugin.hasRequiredInformation(operation, contentType, in.getMObject())){
						requiredFields.put(plugin.getServiceName(), plugin.getRequiredInformation(operation, contentType));
					}
				}
			}
		}
		
		/*if(!requiredFields.isEmpty()){
			throw new IncompleteInformationException(requiredFields);
		}*/
			
		
	}
	
	@Override
	public void postRequest(Request in, Response out, RequestContext context){
		

	}


}
