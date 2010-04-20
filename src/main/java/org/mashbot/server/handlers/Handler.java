package org.mashbot.server.handlers;

import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.types.Request;
import org.mashbot.server.types.RequestContext;
import org.mashbot.server.types.Response;

public interface Handler{
	public void enact(Request in,Response out,RequestContext context) throws MashbotException; 
	
	public void preRequest(Request in,Response out,RequestContext context) throws MashbotException; 
	public void postRequest(Request in,Response out,RequestContext context) throws MashbotException; 

}
