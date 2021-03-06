package org.mashbot.server.handlers;

import org.apache.commons.lang.NotImplementedException;
import org.mashbot.server.types.Request;
import org.mashbot.server.types.RequestContext;
import org.mashbot.server.types.Response;

public class ChainableHandler implements Handler {
	public void enact(Request in,Response out,RequestContext context){
		this.preRequest(in, out, context);
		if(next != null){
			next.enact(in, out, context);
		}
		this.postRequest(in, out, context);
	}

	public void postRequest(Request in, Response out, RequestContext context) {
		throw new NotImplementedException();		
	}

	public void preRequest(Request in, Response out, RequestContext context) {
		throw new NotImplementedException();
	}
	
	public void setNext(ChainableHandler next){
		this.next = next;
	}
	
	ChainableHandler next = null;
}
