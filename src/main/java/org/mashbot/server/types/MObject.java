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
import org.apache.commons.logging.*;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

@XmlRootElement
public class MObject {
	
	private Log log = LogFactory.getLog(getClass());
		
	/** 
	 * Creates a new MOBject.
	 */
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
		QUERYTYPE("querytype"), 
		TRUE("true"),
		FALSE("false"), 
		ALBUMDESC(GenericFieldStorage.join(ALBUM,"desc")), 
		BODY("body");
		
		
		
		
		Field(String label){
			this.label = label;
		}
		private String label;
	}
	
	/**
	 * 
	 * @return A list of the queries contained in the MObject
	 * @throws InvalidFieldException
	 */
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
	
	/**
	 * 
	 * @param key  The name of the field to access
	 * @return The list of values associated with key
	 */
	public List<String> getField(String key){
		if(this.lists.containsKey(key.toLowerCase())){
			return this.lists.get(key.toLowerCase());
		} else {
			return new ArrayList<String>();
		}
	}
	
	/**
	 * Returns the first value associated with the key. Useful for when the field will only ever contain a single value.
	 * 
	 * @param key  The name of the field to access
	 * @return  The first value associated with the key.
	 */
	public String getStringField(String key){
		List<String> res = this.getField(key);
		if(res.size() == 0){
			return "";
		} else {
			return res.get(0);
		}
	}
	
	/**
	 * 
	 * @param key  The name of the field to access 
	 * @param serviceName  The name of the service whose parameters need to be accessed
	 * @return  The string value associated with key and serviceName
	 */
	public String getStringField(Field key, String serviceName){
		return this.getStringField(GenericFieldStorage.join(key.toString(), serviceName));
	}
	
	/**
	 * 
	 * @param key  The key to associate the value list with
	 * @param value  The list of values associated with the key.
	 */
	
	public void putField(String key, List<String> value){
		this.lists.put(key.toLowerCase(),value); 
	}
	
	/**
	 * 
	 * Sets the value asociated with key to a singleton list containing value
	 * 
	 * @param key  the key to associate the value with
	 * @param value  the value to associate
	 */
	public void putField(String key, String value){
		List<String> tmp = new ArrayList<String>();
		tmp.add(value);
		this.putField(key,tmp);
	}
	
	/**
	 * 
	 * @param key  The field value to access
	 * @return  A list of values associated with field
	 */
	public List<String> getField(Field key){
		return this.getField(key.toString());
	}
	
	/**
	 * 
	 * @param key  the name of the field to access
	 * @param service  the name of the service the field is associated with
	 * @return  returns the list of values of the field associated with service
	 */
	public List<String> getField(Field key, String service){
		return this.getField(GenericFieldStorage.join(key.toString(),service));
	}
	
	/**
	 * 
	 * @param key the name of the field to access
	 * @param service the name of the service the value is associated with
	 * @param username  the username on the service the value is associated with
	 * @return the value associated with the given key, service, and username
	 */
	public List<String> getField(Field key, String service, String username){
		return this.getField(key, GenericFieldStorage.join(service,username));
	}
	
	/**
	 * 
	 * @param key the key to associate the string value with
	 * @param value the value to associate with the key
	 */
	public void putField(Field key,String value){
		this.putField(key.toString(), value);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @param service
	 */
	public void putField(Field key,String value,String service){
		this.putField(GenericFieldStorage.join(key.toString(),service), value);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @param service
	 * @param username
	 */
	public void putField(Field key,String value,String service, String username){
		this.putField(key, value,GenericFieldStorage.join(service, username));
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @param service
	 * @param username
	 * @param id
	 */
	public void putField(Field key,String value,String service, String username, int id){
		this.putField(key, value,service,GenericFieldStorage.join(username, Integer.toString(id)));
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @param service
	 * @param username
	 * @param id
	 */
	public void putField(Field key,String value,String service, String username, long id){
		this.putField(key, value,service,GenericFieldStorage.join(username, Long.toString(id)));
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putField(Field key, List<String> value){
		this.putField(key.toString(), value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @param service
	 */
	public void putField(Field key,List<String> value,String service){
		this.putField(GenericFieldStorage.join(key.toString(),service), value);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @param service
	 * @param username
	 */
	public void putField(Field key,List<String> value,String service, String username){
		this.putField(key, value,GenericFieldStorage.join(service, username));
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @param service
	 * @param username
	 * @param id
	 */
	public void putField(Field key,List<String> value,String service, String username, int id){
		this.putField(key, value,GenericFieldStorage.join(username, Integer.toString(id)));
	}

	/**
	 * 
	 * @return
	 */
	@XmlTransient
	public Map<String,List<String>> getFields(){
		return this.lists;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean containsField(Field key){
		return this.containsField(key.toString());
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean containsField(String key){
		return this.lists.containsKey(key.toLowerCase());
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> getServices(){
		return this.getField(Field.SERVICES.toString());
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> getServicesOff(){
		return this.getField(Field.SERVICESOFF.toString());
	}
	
	/**
	 * 
	 * @param available
	 * @return
	 */
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

	/**
	 * 
	 * @return
	 */
	public List<Property> getContext() {
		try {
			this.context = awesome.marshal(this.lists);
		} catch (Exception e) {
			this.context = new ArrayList<Property>();
		}
				
		return this.context;
	}

	/**
	 * 
	 * @param authInfo
	 */
	public void setContext(List<Property> authInfo) {
		this.context = context;
		try {
			this.lists = awesome.unmarshal(context);
		} catch (Exception e) {
			this.lists = new HashMap<String, List<String>>();
		}
	}
	
	private static PropertyMapAdapter awesome = new PropertyMapAdapter();

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getStringField(Field key) {
		return this.getStringField(key.toString());
	}
	
	/**
	 * 
	 * @param b
	 */
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

	/**
	 * 
	 * @param field
	 * @return
	 */
	public boolean containsKey(String field){
		return this.lists.containsKey(field);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void appendField(String key, String value){
		if (this.lists.containsKey(key.toLowerCase())){
			this.lists.get(key.toLowerCase()).add(value);
		}
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void appendField(Field key, String value) {
		this.appendField(key.toString(), value);	
	}
}
