package org.mashbot.server.types;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ServiceCredential {
	public String key;
	public String secret;
	public String method;
}
