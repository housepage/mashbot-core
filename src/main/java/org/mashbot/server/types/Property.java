package org.mashbot.server.types;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="")
@XmlRootElement(name="")
public class Property {
	
	public Property(){
		this.key = "";
		this.value = new ArrayList<String>();
	}
	
	@XmlElement
	public String key;
	
	@XmlElement
	public List<String> value;
}