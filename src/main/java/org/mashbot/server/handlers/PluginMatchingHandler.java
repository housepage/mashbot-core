package org.mashbot.server.handlers;

import java.util.List;

import org.mashbot.server.plugins.Plugin;
import org.mashbot.server.plugins.PluginManager;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.Request;
import org.mashbot.server.types.RequestContext;
import org.mashbot.server.types.Response;
import org.mashbot.server.web.MashbotService.Operation;

public class PluginMatchingHandler extends ChainableHandler {

	@Override
	public void postRequest(Request in, Response out, RequestContext context) {
		
	}

	@Override
	public void preRequest(Request in, Response out, RequestContext context) {
		MObject current = in.getMObject();
		
		String operation = in.getOperation();
		String contentType = in.getContentType();
		
		PluginManager manager = context.getPluginManager();
		List<Plugin> plugins = manager.getPlugins(operation, contentType);
		
		context.setPlugins(plugins);
	}
	
}
