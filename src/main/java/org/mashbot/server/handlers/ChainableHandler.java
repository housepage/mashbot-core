package org.mashbot.server.handlers;

import org.apache.commons.lang.NotImplementedException;
import org.mashbot.server.exceptions.IncompleteInformationException;
import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.types.Request;
import org.mashbot.server.types.RequestContext;
import org.mashbot.server.types.Response;

public abstract class ChainableHandler implements Handler {
	
	/**
	 * Calls preRequest on this handler, enact on the next, and then postRequest on this handler
	 * 
	 * @in  The request being handled
	 * @out The response to the request
	 * @context The request of the context
	 */
	public void enact(Request in,Response out,RequestContext context) throws MashbotException{
		this.preRequest(in, out, context);
		if(next != null){
			next.enact(in, out, context);
		}
		this.postRequest(in, out, context);
	}

	public abstract void preRequest(Request in, Response out, RequestContext context) throws MashbotException;

	public abstract void postRequest(Request in, Response out, RequestContext context) throws MashbotException;

	public void setNext(ChainableHandler next){
		this.next = next;
	}
	
	ChainableHandler next = null;
}
