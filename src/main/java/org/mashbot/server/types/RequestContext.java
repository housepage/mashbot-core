package org.mashbot.server.types;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.lang.Class;

import org.apache.commons.logging.LogFactory;
import org.mashbot.server.types.ServiceCredential;
import org.mashbot.server.auth.AuthenticationManager;
import org.mashbot.server.plugins.Plugin;
import org.mashbot.server.plugins.PluginManager;
import org.apache.commons.logging.Log;

public class RequestContext extends GenericFieldStorage {
	Map<String,Object> context;
	Log log = LogFactory.getLog(getClass());
	
	public enum Field{
		PLUGINS("plugins"),
		SERVICECREDENTIALS("credentials"),
		PLUGINMANAGER("pluginmanager"),
		AUTHMANAGER("authmanager");
		Field(String label){
			this.label = label;
		}
		public String toString(){
			return label;
		}
		private String label;
	}
	
	public RequestContext(){
		super();
	}
	
	public Object getField(Field key){
		return super.getField(key.toString()																																			);
	}
	
	public void putField(Field key,Object value){
		super.putField(key.toString(), value);
	}
	
	public List<Plugin> getPlugins(){
		return (List<Plugin>) getField(Field.PLUGINS);
	}
	
	public void setPlugins(List<Plugin> plugins){
		putField(Field.PLUGINS,plugins);
	}

	public Map<String, List<ServiceCredential>> getServiceCredentials() {
		return (Map<String, List<ServiceCredential>>) getField(Field.SERVICECREDENTIALS);
	}
	
	public void setServiceCredentials(Map<String,ServiceCredential> creds){
		putField(Field.SERVICECREDENTIALS, creds);
	}

	public PluginManager getPluginManager() {
		return (PluginManager) getField(Field.PLUGINMANAGER);
	}
	
	public void setPluginManager(PluginManager in) {
		putField(Field.PLUGINMANAGER,in);
	}

	public void setAuthenticationManager(AuthenticationManager in) {
		putField(Field.AUTHMANAGER,in);
	}

	public AuthenticationManager getAuthenticationManager() {
		return (AuthenticationManager) getField(Field.AUTHMANAGER);
	}
}
