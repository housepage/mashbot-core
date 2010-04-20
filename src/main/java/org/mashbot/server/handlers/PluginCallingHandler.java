package org.mashbot.server.handlers;

import org.mashbot.server.plugins.Plugin;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.Request;
import org.mashbot.server.types.RequestContext;
import org.mashbot.server.types.Response;
import org.mashbot.server.types.ServiceCredential;
import org.mashbot.server.types.RequestContext.Field;
import org.mortbay.jetty.security.Credential;

import java.util.List;
import java.util.Map;

public class PluginCallingHandler extends ChainableHandler {

	@Override
	public void postRequest(Request in, Response out, RequestContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preRequest(Request in, Response out, RequestContext context) throws Exception {
		MObject incoming = in.getMObject();
		String operation = (String) in.getField(Request.Field.OPERATION);
		String contentType = (String) in.getField(Request.Field.CONTENTTYPE);
		
		List<Plugin> plugins = context.getPlugins();
		Map<String, List<ServiceCredential>> credentialMap = context.getServiceCredentials();
		for(Plugin plugin : plugins){
			List<ServiceCredential> credentials = credentialMap.get(plugin.getServiceName());
			for(ServiceCredential credential : credentials){
				plugin.run(operation, contentType, incoming, credential);
			}
			
		}
		
	}

}
