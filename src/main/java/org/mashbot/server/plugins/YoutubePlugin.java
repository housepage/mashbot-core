package org.mashbot.server.plugins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mashbot.server.exceptions.IncompleteSecretInformationException;
import org.mashbot.server.exceptions.InvalidConfigFileException;
import org.mashbot.server.exceptions.InvalidFieldException;
import org.mashbot.server.exceptions.InvalidRequestException;
import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.exceptions.MissingAuthenticationException;
import org.mashbot.server.exceptions.UndownloadableContentException;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.ServiceCredential;

import twitter4j.Twitter;

public class YoutubePlugin extends Plugin {

	protected static String serviceName = "youtube";
	private static final Map<String, List<String>> supported;
    
    static {
        Map<String, List<String>> aSupported = new HashMap<String,List<String>>();
        List<String> photos = new ArrayList<String>();
		photos.add("push");
		photos.add("pull");
		photos.add("edit");
		photos.add("delete");
		aSupported.put("video", Collections.unmodifiableList(photos));
        supported = Collections.unmodifiableMap(aSupported);
    }
	@Override
	public List<String> getRequiredInformation(String operation,
			String contentType) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getServiceName() {
		return YoutubePlugin.serviceName;
	}

	@Override
	protected MObject delete(String contentType, MObject content)
			throws MashbotException, IncompleteSecretInformationException,
			InvalidConfigFileException, InvalidFieldException,
			UndownloadableContentException, InvalidRequestException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected MObject edit(String contentType, MObject content)
			throws MashbotException, IncompleteSecretInformationException,
			InvalidConfigFileException, InvalidFieldException,
			UndownloadableContentException, InvalidRequestException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected MObject pull(String contentType, MObject content)
			throws MashbotException, IncompleteSecretInformationException,
			InvalidConfigFileException, InvalidFieldException,
			UndownloadableContentException, InvalidRequestException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected MObject push(String contentType, MObject content)
			throws MashbotException, IncompleteSecretInformationException,
			InvalidConfigFileException, InvalidFieldException,
			UndownloadableContentException, InvalidRequestException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setup(ServiceCredential credential, String operation)
			throws IncompleteSecretInformationException,
			InvalidConfigFileException, MissingAuthenticationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, List<String>> getSupported() {
		// TODO Auto-generated method stub
		return null;
	}

}
