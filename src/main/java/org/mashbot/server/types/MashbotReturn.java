package org.mashbot.server.types;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MashbotReturn {
	public int x;
	public int y;
	public Map<String,ServiceCredential> c;
}
