package org.mashbot.server.types;

import java.util.HashMap;
import java.util.Map;

public abstract class GenericFieldStorage {
	Map<String,Object> context;
	
	public GenericFieldStorage(){
		context = new HashMap<String,Object>();
	}
	
	public Object getField(String key){
		return context.get(key);
	}
	
	public void putField(String key,Object value){
		context.put(key, value);
	}
}
