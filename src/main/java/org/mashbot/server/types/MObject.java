package org.mashbot.server.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.cxf.jaxrs.utils.HttpUtils;
import org.mashbot.server.types.GenericFieldStorage;

@XmlRootElement
public class MObject {
		
	public MObject() {
		this.context = new HashMap<String, List<String>>();
		
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
		SUCCESS("success"), 
		FAILURE("failure");
		Field(String label){
			this.label = label;
		}
		private String label;
	}
	
	public List<String> getField(String key){
		return this.context.get(key);
	}
	
	public void putField(String key, List<String> value){
		this.context.put(key,value); 
	}
	
	public List<String> getField(Field key){
		return this.getField(key.toString());
	}
	
	public void putField(Field key, List<String> value){
		this.putField(key.toString(), value);
	}
	
	public void putField(Field key,List<String> value,String service){
		this.putField(GenericFieldStorage.join(key.toString(),service), value);
	}

	public List<String> getServices(){
		return this.getField(Field.SERVICES.toString());
	}
	
	public List<String> getServicesOff(){
		return this.getField(Field.SERVICESOFF.toString());
	}
	
	public List<String> getServices(List<String> available){
		List<String> toCall = new ArrayList<String>();
		
		if(this.context.containsKey(Field.SERVICES)){
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
		
		if(this.context.containsKey(Field.SERVICESOFF)){
			List<String> specified = this.getServicesOff();
			for(String service : available){
				if(specified.contains(service)){
					toCall.remove(service);
				}
			}
		}
		
		return toCall;
	}
	
  public void putField(Field key, String value){
		this.putField(key.toString(),value);
}
	
	public void putField(String key, String value){
		List<String> tmp = new ArrayList<String>();
		tmp.add(value);
		this.putField(key,tmp);
}

	@XmlElement(name="context")
	public Map<String,List<String>> context;

	public boolean containsKey(String field){
		return this.context.containsKey(field);
	}
	
	public void appendField(String key, String value){
		if (this.context.containsKey(key)){
			context.get(key).add(value);
		}
	}
}
