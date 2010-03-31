package org.mashbot.server.types;

import java.util.HashMap;
import java.util.Map;

import org.mashbot.server.types.RequestContext.Field;

public abstract class GenericFieldStorage {
	Map<String,Object> context;
	
	public GenericFieldStorage(){
		context = new HashMap<String,Object>();
	}
	
	public Object getField(String key){
		return context.get(key);
	}
	public Object getField(Field key){
		return context.get(key.toString());
	}
	
	public void putField(String key,Object value){
		context.put(key, value);
	}
	public void putField(Field key,Object value){
		context.put(key.toString(), value);
	}
}
