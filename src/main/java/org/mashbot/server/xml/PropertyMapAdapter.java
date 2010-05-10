package org.mashbot.server.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mashbot.server.types.Property;
import org.mashbot.server.types.ServiceCredential;
import java.util.HashMap;
import java.util.Map.Entry;

public class PropertyMapAdapter extends
		XmlAdapter<List<Property>,Map<String,List<String>>> {

	/* @Override
	public HashMap<String, ArrayList<ServiceCredential>> marshal(
			List<AllServiceCredentials> v) throws Exception {
		HashMap ret = new HashMap<String,ArrayList<ServiceCredential>>();
		for(AllServiceCredentials i : v){
			ret.put(i.service,i.credentials);
		}
		
		return ret;
	}

	@Override
	public List<AllServiceCredentials> unmarshal(
			HashMap<String, ArrayList<ServiceCredential>> v) throws Exception {
		List<AllServiceCredentials> all = new ArrayList<AllServiceCredentials>();
		for(Entry<String,ArrayList<ServiceCredential>> i : v.entrySet()){
			AllServiceCredentials cur = new AllServiceCredentials();
			cur.service = i.getKey();
			cur.credentials = i.getValue();
			all.add(cur);
		}
		return all;
	}*/

	@Override
	public List<Property> marshal(
			Map<String, List<String>> v) throws Exception {
		/*AllCredentials all = new AllCredentials();*/
		List<Property> all = new ArrayList<Property>();
		for(Entry<String,List<String>> i : v.entrySet()){
			Property cur = new Property();
			cur.key = i.getKey();
			cur.value = i.getValue();
			all.add(cur);
		}
		return all;
	}

	@Override
	public Map<String, List<String>> unmarshal(
			List<Property> v) throws Exception {
		HashMap<String, List<String>> ret = new HashMap<String,List<String>>();
		for(Property i : v){
			ret.put(i.key,new ArrayList<String>(i.value));
		}
		
		return ret;
	}

}
