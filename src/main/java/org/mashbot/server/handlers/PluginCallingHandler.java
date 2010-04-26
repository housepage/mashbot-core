package org.mashbot.server.handlers;

import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.plugins.Plugin;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.Request;
import org.mashbot.server.types.RequestContext;
import org.mashbot.server.types.Response;
import org.mashbot.server.types.ServiceCredential;
import org.mashbot.server.types.RequestContext.Field;
import org.mashbot.server.web.MashbotService.Operation;

import java.util.List;
import java.util.Map;

public class PluginCallingHandler extends ChainableHandler {

	@Override
	public void postRequest(Request in, Response out, RequestContext context) throws MashbotException {
		
	}

	@Override
	public void preRequest(Request in, Response out, RequestContext context) throws MashbotException {
		MObject incoming = in.getMObject();
		String operation = in.getOperation();
		String contentType = in.getContentType();

		List<Plugin> plugins = context.getPlugins();
		Map<String, List<ServiceCredential>> credentialMap = context.getServiceCredentials();
		for(Plugin plugin : plugins){
			List<ServiceCredential> credentials = credentialMap.get(plugin.getServiceName());
			for(ServiceCredential credential : credentials){
				plugin.run(operation, contentType, incoming, credential);
			}
		}
		
		System.out.println("Herro");
	}

}
