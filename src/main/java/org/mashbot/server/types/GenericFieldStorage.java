package org.mashbot.server.types;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public abstract class GenericFieldStorage {

	public Map<String,Object> context;
	
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
