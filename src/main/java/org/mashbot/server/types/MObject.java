package org.mashbot.server.types;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.cxf.jaxrs.utils.HttpUtils;

@XmlRootElement
public class MObject extends GenericFieldStorage {
		
	public MObject() {
		super();
	}

	public enum Field{
		NULL("null"), USERNAME("username"), PASSWORD("password");
		Field(String label){
			this.label = label;
		}
		private String label;
	}
	
	public String toString(){
		return "Yum";
	}

	public void putField(Field username, String servicename, String string) {
		
	}
}
