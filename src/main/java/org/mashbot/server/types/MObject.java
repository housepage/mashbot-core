package org.mashbot.server.types;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.cxf.jaxrs.utils.HttpUtils;

@XmlRootElement
public class MObject extends GenericFieldStorage {
	public MObject(String in){
		super();
		System.out.println("Here!:" + HttpUtils.pathDecode(in));
	}
		
	public enum Field{
		NULL("null");
		Field(String label){
			this.label = label;
		}
		private String label;
	}
	
	public String toString(){
		return "Yum";
	}
}
