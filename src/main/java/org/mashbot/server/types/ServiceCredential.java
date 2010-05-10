package org.mashbot.server.types;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement
public class ServiceCredential  {
	
	@XmlElement(required=true)
	public String key;
	
	@XmlElement(required=true)
	public String secret;
	
	@XmlElement(required=true)
	public String method;
}
