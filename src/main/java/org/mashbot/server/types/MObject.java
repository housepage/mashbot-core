package org.mashbot.server.types;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.cxf.jaxrs.utils.HttpUtils;
import org.mashbot.server.types.Request.Field;

@XmlRootElement(name="Mashbot",namespace="http://mashbot.heroku.com/mashbot")
public class MObject extends GenericFieldStorage {
		
	public MObject() {
		super();
	}

	public enum Field{
		USERNAME("username"), 
		PASSWORD("password");
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
}
