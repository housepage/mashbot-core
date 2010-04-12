package org.mashbot.server.handlers;

import org.mashbot.server.plugins.Plugin;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.Request;
import org.mashbot.server.types.RequestContext;
import org.mashbot.server.types.Response;
import org.mashbot.server.types.ServiceCredential;
import org.mashbot.server.types.RequestContext.Field;
import java.util.List;
import java.util.Map;

public class PluginCallingHandler extends ChainableHandler {

	@Override
	public void postRequest(Request in, Response out, RequestContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preRequest(Request in, Response out, RequestContext context) {
		MObject incoming = in.getMObject();
		
		List<Plugin> plugins = context.getPlugins();
		Map<String, ServiceCredential> credentials = context.getServiceCredentials();
	}

}
