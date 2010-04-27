package org.mashbot.server.plugins;

import java.util.List;
import java.util.Map;

import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.ServiceCredential;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookJsonRestClient;
import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;

public class FacebookStatusPlugin extends Plugin {

	@Override
	public List<String> getRequiredInformation(String operation,
			String contentType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServiceName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<String>> getSupported() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasRequiredInformation(String operation, String contentType,
			MObject content) {
		return true;
	}

	@Override
	public MObject run(String operation, String contentType, MObject content,
			ServiceCredential credential) throws MashbotException {
		String twitterID = credential.key;
    	String twitterPassword = credential.secret;
    	String latestStatus = ((ElementNSImpl) content.getField("STATUS")).getTextContent();
		FacebookJsonRestClient client = new FacebookJsonRestClient(twitterID,twitterPassword);
		try {
			client.users_setStatus(latestStatus);
		} catch (FacebookException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String apiKey;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
}
