package org.mashbot.server.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class MapXmlAdapter<K,V> extends XmlAdapter<XmlMap<K,V>,Map<K,V>> {

	public XmlMap<K, V> marshal(Map<K, V> in) throws Exception {
		XmlMap<K,V> out = new XmlMap<K, V>();
		out.entries = new ArrayList<XmlMapEntry<K,V>>();
	
		for(Entry<K, V> i : in.entrySet()){
			XmlMapEntry<K, V> cur = new XmlMapEntry<K, V>();
			cur.key = i.getKey();
			cur.value = i.getValue();
			out.entries.add(cur);
		}
		
		return out;
	}

	public Map<K, V> unmarshal(XmlMap<K, V> in) throws Exception {
		Map<K,V> out = new HashMap<K,V>();
		
		for(XmlMapEntry<K, V> i : in.entries){
			out.put(i.key,i.value);
		}
		
		return out;
	}

}
