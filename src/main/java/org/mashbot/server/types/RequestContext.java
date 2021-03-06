package org.mashbot.server.types;

import java.util.HashMap;
import java.util.Map;

public class RequestContext extends GenericFieldStorage {
	Map<String,Object> context;
	
	public enum Field{
		OPERATION("operation"),
		CONTENTTYPE("contentType");
		Field(String label){
			this.label = label;
		}
		private String label;
	}
	
	public RequestContext(){
		super();
	}
}
