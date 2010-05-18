package org.mashbot.server.handlers;

import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.types.Request;
import org.mashbot.server.types.RequestContext;
import org.mashbot.server.types.Response;

public interface Handler{
	
	/**
	 * runs the handler
	 * 
	 * @param in  The request being handled
	 * @param out  The current response to the request
	 * @param context  The request context
	 * @throws MashbotException
	 */
	public void enact(Request in,Response out,RequestContext context) throws MashbotException; 
	
	
	/**
	 * Does things that need to be done before calling the next handler in the chain
	 * 
	 * @param in  The request being handled
	 * @param out  The response to the request
	 * @param context  The request context
	 * @throws MashbotException
	 */
	public void preRequest(Request in,Response out,RequestContext context) throws MashbotException; 
	
	/**
	 * Does things that need to be done before calling the next handler in the chain
	 * 
	 * @param in  The request being handled
	 * @param out  The response to the request
	 * @param context  The request context
	 * @throws MashbotException
	 */
	public void postRequest(Request in,Response out,RequestContext context) throws MashbotException; 

}
