package org.mashbot.server.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlValue;

public class XmlMapEntry<K,V> {
	
	@XmlAttribute
    public K key; 

    @XmlValue()
    public V value;
}
