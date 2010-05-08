package org.mashbot.server.types;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement
<<<<<<< HEAD
public class ServiceCredential  {
	
	@XmlElement
	public String key;
	
	@XmlElement
	public String secret;
	
	@XmlElement
=======
public class ServiceCredential {
	public String key;
	public String secret;
>>>>>>> gall-plugins
	public String method;
}
