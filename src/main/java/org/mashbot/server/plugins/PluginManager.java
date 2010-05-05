package org.mashbot.server.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.mashbot.server.web.MashbotService.Operation;
import org.apache.commons.logging.Log;

public class PluginManager {
	private Map<String,Map<String,List<Plugin>>> plugins;
	private Log log = LogFactory.getLog(getClass());

	public Map<String, Map<String, List<Plugin>>> getPlugins() {
		return plugins;
	}

	public void setPlugins(Map<String, Map<String, List<Plugin>>> plugins) {
		this.plugins = plugins;
	}
	
	public List<Plugin> getPlugins(String operation, String contentType){
		log.warn(plugins.get(contentType));
		log.warn("Operation:"+operation);
		log.warn("Content Type:"+contentType);
		if(plugins.containsKey(contentType) && plugins.get(contentType).containsKey(operation)){
			log.warn("Should go here");
			log.warn(plugins.get(contentType).get(operation));
			return plugins.get(contentType).get(operation);
		} else {
			log.warn("Is probably going here");
			return new ArrayList<Plugin>();
		}
	}

	public List<String> getSupportedServices(String operation,
			String contentType) {
		if(plugins.containsKey(contentType) && plugins.get(contentType).containsKey(operation)){
			List<String> supported = new ArrayList<String>();
			for(Plugin i : plugins.get(contentType).get(operation)){
				supported.add(i.getServiceName());
			}
			return supported;
		} else {
			return new ArrayList<String>();
		}
	}
}
