package org.mashbot.server.types;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.mashbot.server.plugins.Plugin;

public class RequestContext extends GenericFieldStorage {
	Map<String,Object> context;
	
	public enum Field{
		PLUGINS("plugins");
		Field(String label){
			this.label = label;
		}
		private String label;
	}
	
	public RequestContext(){
		super();
	}
	
}
