package org.mashbot.server.types;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.lang.Class;

import org.mashbot.server.types.ServiceCredential;
import org.mashbot.server.plugins.Plugin;

public class RequestContext extends GenericFieldStorage {
	Map<String,Object> context;
	
	public enum Field{
		PLUGINS("plugins"),
		SERVICECREDENTIALS("credentials");
		Field(String label){
			this.label = label;
		}
		private String label;
	}
	
	public RequestContext(){
		super();
	}
	
	public Object getField(Field key){
		return context.get(key);
	}
	
	public void putField(Field key,Object value){
		context.put(key.toString(), value);
	}
	
	public List<Plugin> getPlugins(){
		return (List<Plugin>) getField(Field.PLUGINS);
	}
	
	public void setPlugins(List<Plugin> plugins){
		putField(Field.PLUGINS,plugins);
	}

	public Map<String,ServiceCredential> getServiceCredentials() {
		return (Map<String, ServiceCredential>) getField(Field.SERVICECREDENTIALS);
	}
	
	public void setServiceCredentials(Map<String,ServiceCredential> creds){
		putField(Field.SERVICECREDENTIALS, creds);
	}
}
