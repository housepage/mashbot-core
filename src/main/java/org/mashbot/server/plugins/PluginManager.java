package org.mashbot.server.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mashbot.server.web.MashbotService.Operation;

public class PluginManager {
	private Map<Operation,Map<String,List<Plugin>>> plugins;

	public Map<Operation, Map<String, List<Plugin>>> getPlugins() {
		return plugins;
	}

	public void setPlugins(Map<Operation, Map<String, List<Plugin>>> plugins) {
		this.plugins = plugins;
	}
	
	public List<Plugin> getPlugins(Operation operation, String contentType){
		if(plugins.containsKey(operation) && plugins.get(operation).containsKey(contentType)){
			return plugins.get(operation).get(contentType);
		} else {
			return new ArrayList<Plugin>();
		}
	}
}
