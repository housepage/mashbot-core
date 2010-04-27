package org.mashbot.server.types;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.cxf.jaxrs.utils.HttpUtils;
import org.mashbot.server.types.Request.Field;

@XmlRootElement(name="Mashbot",namespace="http://mashbot.heroku.com/mashbot")
public class MObject extends GenericFieldStorage {
		
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
		SUCCESS("success"), 
		FAILURE("failure");
		Field(String label){
			this.label = label;
		}
		private String label;
	}
	
	public Object getField(Field key){
		return this.getField(key.toString());
	}
	
	public void putField(Field key,Object value){
		this.putField(key.toString(), value);
	}
	
	public Object getField(Field key, String service){
		return this.getField(key.toString());
	}
	
	public void putField(Field key,String service,Object value){
		this.putField(GenericFieldStorage.join(key.toString(),service), value);
	}
	
	public List<String> getServices(){
		return this.getStringListField(Field.SERVICES.toString());
	}
	
	public List<String> getServicesOff(){
		return this.getStringListField(Field.SERVICESOFF.toString());
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
}
