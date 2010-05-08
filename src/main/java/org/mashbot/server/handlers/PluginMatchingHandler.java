package org.mashbot.server.handlers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Log4JLogger;
import org.mashbot.server.plugins.Plugin;
import org.mashbot.server.plugins.PluginManager;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.Request;
import org.mashbot.server.types.RequestContext;
import org.mashbot.server.types.Response;
import org.mashbot.server.web.MashbotService.Operation;

public class PluginMatchingHandler extends ChainableHandler {
	
	protected Log log = LogFactory.getLog(getClass());
	
	@Override
	public void postRequest(Request in, Response out, RequestContext context) {
		log.warn("Exiting PluginMatchingHandler");
	}

	@Override
	public void preRequest(Request in, Response out, RequestContext context) {
		MObject current = in.getMObject();
		
		String operation = in.getOperation();
		String contentType = in.getContentType();
		
		log.warn("Entering PluginMatchingHandler");
		
		PluginManager manager = context.getPluginManager();
		List<Plugin> allplugins = manager.getPlugins(operation, contentType);
		List<Plugin> calledplugins = new ArrayList<Plugin>();
		List<String> requested = current.getServices(manager.getSupportedServices(operation, contentType));
		log.warn(requested);
		for(Plugin i : allplugins){
			log.warn(i.getServiceName());
			log.warn(requested.contains(i.getServiceName()));
			if(requested.contains(i.getServiceName())){
				calledplugins.add(i);
				log.warn("Adding plugin for: "+i.getServiceName());
			}
		}
		
		context.setPlugins(calledplugins);
	}
	
}
