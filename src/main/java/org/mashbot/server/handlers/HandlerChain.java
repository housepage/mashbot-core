package org.mashbot.server.handlers;

import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.types.Request;
import org.mashbot.server.types.Response;
import org.springframework.context.support.AbstractApplicationContext;
import org.mashbot.server.types.RequestContext;
import org.springframework.web.bind.annotation.RequestMapping;

public interface HandlerChain{
	
	/**
	 * 
	 * Enacts the handler chain
	 * 
	 * @param request  The request being handled
	 * @param response  The response to the request
	 * @param context  The context of the request
	 * @throws MashbotException
	 */
	public void enact(Request request, Response response, RequestContext context) throws MashbotException;
}
