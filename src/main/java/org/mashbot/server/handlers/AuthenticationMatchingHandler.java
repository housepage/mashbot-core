package org.mashbot.server.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.mashbot.server.auth.AuthenticationManager;
import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.plugins.Plugin;
import org.mashbot.server.types.Request;
import org.mashbot.server.types.RequestContext;
import org.mashbot.server.types.Response;
import org.mashbot.server.types.ServiceCredential;
import org.mashbot.server.types.UserAuthenticationInformation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AuthenticationMatchingHandler extends ChainableHandler {

	Log log = LogFactory.getLog(getClass()); 
	
	@Override
	public void postRequest(Request in, Response out, RequestContext context)
			throws MashbotException {

	}

	@Override
	public void preRequest(Request in, Response out, RequestContext context)
			throws MashbotException {
		List<Plugin> plugins = context.getPlugins();
		List<String> services = new ArrayList<String>();
		for(Plugin i : plugins){
			services.add(i.getServiceName());
		}
		
		Map<String, List<ServiceCredential>> credentialMap = new HashMap<String,List<ServiceCredential>>();
		
		UUID token = in.getToken();
		
		AuthenticationManager authMan = context.getAuthenticationManager();
		UserAuthenticationInformation userAuth = authMan.listAuthenticationInformation(token);
		if(userAuth != null) {
			log.warn(userAuth);
			log.warn(token);
			
			Map<String,List<ServiceCredential>> credentials = userAuth.getCredentials();
			for(Entry<String,List<ServiceCredential>> i : credentials.entrySet()){
				if(services.contains(i.getKey())){
					if(credentialMap.containsKey(i.getKey())){
						for(ServiceCredential j : i.getValue()){
							credentialMap.get(i.getKey()).add(j);
						}
					} else {
						credentialMap.put(i.getKey(),i.getValue());
					}
					log.warn("Adding credentials for: " + i.getKey());
				}
			}
		}
		
		context.setServiceCredentials(credentialMap);
	}

}
