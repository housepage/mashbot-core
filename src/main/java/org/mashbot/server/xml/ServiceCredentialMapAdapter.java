package org.mashbot.server.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.mashbot.server.types.ServiceCredential;
import java.util.HashMap;
import java.util.Map.Entry;

public class ServiceCredentialMapAdapter extends
		XmlAdapter<List<AllServiceCredentials>,Map<String,List<ServiceCredential>>> {

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
	public List<AllServiceCredentials> marshal(
			Map<String, List<ServiceCredential>> v) throws Exception {
		/*AllCredentials all = new AllCredentials();*/
		List<AllServiceCredentials> all = new ArrayList<AllServiceCredentials>();
		for(Entry<String,List<ServiceCredential>> i : v.entrySet()){
			AllServiceCredentials cur = new AllServiceCredentials();
			cur.service = i.getKey();
			cur.credentials = i.getValue();
			all.add(cur);
		}
		return all;
	}

	@Override
	public Map<String, List<ServiceCredential>> unmarshal(
			List<AllServiceCredentials> v) throws Exception {
		HashMap ret = new HashMap<String,ArrayList<ServiceCredential>>();
		for(AllServiceCredentials i : v){
			ret.put(i.service,new ArrayList(i.credentials));
		}
		
		return ret;
	}

}
