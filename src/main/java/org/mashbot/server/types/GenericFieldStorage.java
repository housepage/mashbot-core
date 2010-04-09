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
	public Object getField(Request.Field key){
		return context.get(key.toString());
	}
	public Object getField(RequestContext.Field key){
		return context.get(key.toString());
	}
	public Object getField(Response.Field key){
		return context.get(key.toString());
	}
	public Object getField(MObject.Field key){
		return context.get(key.toString());
	}
	
	public void putField(String key,Object value){
		context.put(key, value);
	}
	public void putField(Request.Field key,Object value){
		context.put(key.toString(), value);
	}
	public void putField(RequestContext.Field key,Object value){
		context.put(key.toString(), value);
	}
	public void putField(Response.Field key,Object value){
		context.put(key.toString(), value);
	}
	public void putField(MObject.Field key,Object value){
		context.put(key.toString(), value);
	}
}
