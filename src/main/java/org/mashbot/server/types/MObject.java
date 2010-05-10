package org.mashbot.server.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.cxf.jaxrs.utils.HttpUtils;
import org.mashbot.server.exceptions.InvalidFieldException;
import org.mashbot.server.types.GenericFieldStorage;
import org.mashbot.server.xml.AllServiceCredentials;
import org.mashbot.server.xml.PropertyMapAdapter;
import org.mashbot.server.xml.ServiceCredentialMapAdapter;
import org.apache.commons.logging.*;;

@XmlRootElement
public class MObject {
	
	private Log log = LogFactory.getLog(getClass());
		
	public MObject() {
		this.lists = new HashMap<String, List<String>>();
	}

	public enum Field{
		SERVICES("services"),
		OFF("off"),
		ON("on"),
		SERVICESOFF(GenericFieldStorage.join(SERVICES, OFF)),
		USERNAME("username"), 
		PASSWORD("password"), 
		STATUS("status"), 
		TAGS(GenericFieldStorage.join(STATUS,"tags")), 
		SUCCESS("success"), 
		FAILURE("failure"), 
		URL("url"),
		CAPTION("caption"), 
		ALBUM("album"), 
		ID("id"), 
		TITLE("title"),
		QUERY("query"),
		QUERYTYPE("querytype");
		
		Field(String label){
			this.label = label;
		}
		private String label;
	}
	
	@XmlTransient
	public List<MashbotQuery> getQueries() throws InvalidFieldException {
		List<MashbotQuery> ret = new ArrayList<MashbotQuery>();
		List<String> queryTypes = this.getField(Field.QUERYTYPE);
		List<String> queries = this.getField(Field.QUERY);
		if(queryTypes.size() != queries.size()){
			if(queryTypes.size() < queries.size()){
				throw new InvalidFieldException(Field.QUERYTYPE);
			} else {
				throw new InvalidFieldException(Field.QUERY);
			}
		}
		
		for(int i = 0; i < queryTypes.size(); i++){
			MashbotQuery cur = new MashbotQuery();
			cur.id = i;
			cur.queryType = new String(queryTypes.get(i));
			cur.query = new String(queries.get(i));
			ret.add(cur);
		}
		
		return ret;
	}
	
	public List<String> getField(String key){
		if(this.lists.containsKey(key.toLowerCase())){
			return this.lists.get(key.toLowerCase());
		} else {
			return new ArrayList<String>();
		}
	}
	
	public String getStringField(String key){
		List<String> res = this.getField(key);
		if(res.size() == 0){
			return "";
		} else {
			return res.get(0);
		}
	}
	
	public void putField(String key, List<String> value){
		this.lists.put(key.toLowerCase(),value); 
	}
	
	public void putField(String key, String value){
		List<String> tmp = new ArrayList<String>();
		tmp.add(value);
		this.putField(key,tmp);
	}
	
	public List<String> getField(Field key){
		return this.getField(key.toString());
	}
	
	public List<String> getField(Field key, String service){
		return this.getField(GenericFieldStorage.join(key.toString(),service));
	}
	
	public List<String> getField(Field key, String service, String username){
		return this.getField(key, GenericFieldStorage.join(service,username));
	}
	
	public void putField(Field key,String value){
		this.putField(key.toString(), value);
	}
	
	public void putField(Field key,String value,String service){
		this.putField(GenericFieldStorage.join(key.toString(),service), value);
	}
	
	public void putField(Field key,String value,String service, String username){
		this.putField(key, value,GenericFieldStorage.join(service, username));
	}
	
	public void putField(Field key,String value,String service, String username, int id){
		this.putField(key, value,GenericFieldStorage.join(username, Integer.toString(id)));
	}
	
	public void putField(Field key, List<String> value){
		this.putField(key.toString(), value);
	}
	
	public void putField(Field key,List<String> value,String service){
		this.putField(GenericFieldStorage.join(key.toString(),service), value);
	}
	
	public void putField(Field key,List<String> value,String service, String username){
		this.putField(key, value,GenericFieldStorage.join(service, username));
	}
	
	public void putField(Field key,List<String> value,String service, String username, int id){
		this.putField(key, value,GenericFieldStorage.join(username, Integer.toString(id)));
	}

	
	@XmlTransient
	public Map<String,List<String>> getFields(){
		return this.lists;
	}

	public boolean containsField(Field key){
		return this.containsField(key.toString());
	}
	
	public boolean containsField(String key){
		return this.lists.containsKey(key);
	}
	
	public List<String> getServices(){
		return this.getField(Field.SERVICES.toString());
	}
	
	public List<String> getServicesOff(){
		return this.getField(Field.SERVICESOFF.toString());
	}
	
	public List<String> getServices(List<String> available){
		List<String> toCall = new ArrayList<String>();
		
		if(this.lists.containsKey(Field.SERVICES)){
			List<String> specified = this.getServices();
			for(String service : available){
				if(specified.contains(service)){
					toCall.add(service);
				}
			}
		} else {
			for(String service : available){
				toCall.add(new String(service));
			}
		}
		
		if(this.lists.containsKey(Field.SERVICESOFF)){
			List<String> specified = this.getServicesOff();
			for(String service : available){
				if(specified.contains(service)){
					toCall.remove(service);
				}
			}
		}
		
		return toCall;
	}

	@XmlTransient
	public Map<String,List<String>> lists;
	
	private List<Property> context;

	public List<Property> getContext() {
		try {
			this.context = awesome.marshal(this.lists);
		} catch (Exception e) {
			this.context = new ArrayList<Property>();
		}
				
		return this.context;
	}

	public void setContext(List<Property> authInfo) {
		this.context = context;
		try {
			this.lists = awesome.unmarshal(context);
		} catch (Exception e) {
			this.lists = new HashMap<String, List<String>>();
		}
	}
	
	private static PropertyMapAdapter awesome = new PropertyMapAdapter();

	public String getStringField(Field key) {
		return this.getStringField(key.toString());
	}
	
	public void join(MObject b){
		for(Entry<String,List<String>>  i : b.getFields().entrySet()){
			if(this.lists.containsKey(i.getKey())){
				for(String j : i.getValue()){
					if(!this.lists.get(i.getKey()).contains(j)){
						this.lists.get(i.getKey()).add(j);
					}
				}
			} else {
				this.lists.put(i.getKey(), new ArrayList<String>(i.getValue()));
			}
		}
	}

	public boolean containsKey(String field) {
		return this.lists.containsKey(field);
	}
}
