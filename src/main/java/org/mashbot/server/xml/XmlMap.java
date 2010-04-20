package org.mashbot.server.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="")
public class XmlMap<K,V> {
	public XmlMap(){
		this.entries = new ArrayList<XmlMapEntry<K,V>>();
	}
	
	@XmlElement
	List<XmlMapEntry<K,V>> entries;
}
