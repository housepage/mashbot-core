package org.mashbot.server.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.mashbot.server.types.MObject.Field;

public abstract class GenericFieldStorage {

	public Map<String,Object> context;
	
	public GenericFieldStorage(){
		this.context = new HashMap<String,Object>();
	}
	
	public Object getField(String key){
		return context.get(key);
	}
	
	public void putField(String key,Object value){
		context.put(key, value); 
	}
	
	public Object getField(String key, String service){
		if(context.containsKey(GenericFieldStorage.join(key, service))){
			return context.get(GenericFieldStorage.join(key, service));
		} else {
			return context.get(key);
		}
	}
	
	public void putField(String key, String service, Object value){
		context.put(GenericFieldStorage.join(key, service), value);
	}
	
	public static String join(Object a, Object b){
		return a.toString() + "." + b.toString();
	}
	
	public static String join3(Object a, Object b, Object c){
		return a.toString() + "." + b.toString() + "." + c.toString();
	}
	
	public List<String> getStringListField(String key){
		List<String> toCall = new ArrayList<String>();
		if( this.context.containsKey(key) && this.getField(key) instanceof List){
			return (List<String>) this.getField(key);
		} else {
			return new ArrayList<String>();
		}
	}
	
	public List<String> getStringListField(String key,String service){
		return getStringListField(GenericFieldStorage.join(key,service));
	}
}
