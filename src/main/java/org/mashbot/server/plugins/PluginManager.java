package org.mashbot.server.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mashbot.server.web.MashbotService.Operation;

public class PluginManager {
	private Map<String,Map<String,List<Plugin>>> plugins;

	public Map<String, Map<String, List<Plugin>>> getPlugins() {
		return plugins;
	}

	public void setPlugins(Map<String, Map<String, List<Plugin>>> plugins) {
		this.plugins = plugins;
	}
	
	public List<Plugin> getPlugins(String operation, String contentType){
		if(plugins.containsKey(operation) && plugins.get(operation).containsKey(contentType)){
			return plugins.get(operation).get(contentType);
		} else {
			return new ArrayList<Plugin>();
		}
	}
}
