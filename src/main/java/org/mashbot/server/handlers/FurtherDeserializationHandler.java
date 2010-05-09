package org.mashbot.server.handlers;

import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.Request;
import org.mashbot.server.types.RequestContext;
import org.mashbot.server.types.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FurtherDeserializationHandler extends ChainableHandler {

	Log log = LogFactory.getLog(getClass());
	
	@Override
	public void postRequest(Request in, Response out, RequestContext context)
			throws MashbotException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preRequest(Request in, Response out, RequestContext context)
			throws MashbotException {
		MObject m = in.getMObject();
		log.warn(m.getFields());
		for(String field : m.getFields()){
			if(m.getField(field) != null){
				log.warn(field+":"+m.getField(field));
			}
		}
	}

}
