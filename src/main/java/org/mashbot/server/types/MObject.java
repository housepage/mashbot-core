package org.mashbot.server.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.cxf.jaxrs.utils.HttpUtils;
import org.mashbot.server.types.Request.Field;

@XmlRootElement
public class MObject {
		
	public MObject() {
		super();
	}

	public enum Field{
		SERVICES("services"),
		OFF("off"),
		ON("on"),
		SERVICESOFF(GenericFieldStorage.join(SERVICES, OFF)),
		USERNAME("username"), 
		PASSWORD("password"), 
		STATUS("status"), 
		TAGS(GenericFieldStorage.join(STATUS,"tags")), 
		SUCCESS("success");
		
		Field(String label){
			this.label = label;
		}
		private String label;
	}
	
	public String getString(String key){
		return strings.get(key);
	}
	
	public void putString(String key,String value){
		strings.put(key, value);
	}
	
	public List<String> getList(String key){
		return lists.get(key);
	}
	
	public void putList(String key, List<String> value){
		lists.put(key,value); 
	}
	
	public String getString(Field key){
		return this.getString(key.toString());
	}
	
	public List<String> getList(Field key){
		return this.getList(key.toString());
	}
	
	public void putString(Field key, String value){
		this.putString(key.toString(), value);
	}
	
	public void putList(Field key, List<String> value){
		this.putString(key.toString(), value);
	}
	
	public void putList(Field key,List<String> value,String service){
		this.putList(GenericFieldStorage.join(key.toString(),service), value);
	}
	
	public void putString(Field key,String value,String service){
		this.putString(GenericFieldStorage.join(key.toString(),service), value);
	}
	
	public List<String> getServices(){
		return this.getList(Field.SERVICES.toString());
	}
	
	public List<String> getServicesOff(){
		return this.getList(Field.SERVICESOFF.toString());
	}
	
	public List<String> getServices(List<String> available){
		List<String> toCall = new ArrayList<String>();
		
		if(this.lists.containsKey(Field.SERVICES)){
			List<String> specified = this.getServices();
			for(String service : available){
				if(specified.contains(service)){
					toCall.add(service);
				}
			}
		} else {
			for(String service : available){
				toCall.add(new String(service));
			}
		}
		
		if(this.lists.containsKey(Field.SERVICESOFF)){
			List<String> specified = this.getServicesOff();
			for(String service : available){
				if(specified.contains(service)){
					toCall.remove(service);
				}
			}
		}
		
		return toCall;
	}
	
	public Map<String,String> strings;
	public Map<String,List<String>> lists;
}
