package org.mashbot.server.handlers;

import java.util.List;

import org.mashbot.server.types.RequestContext;
import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.handlers.ChainableHandler;
import org.mashbot.server.types.Request;
import org.mashbot.server.types.Response;
import org.springframework.context.support.AbstractApplicationContext;

public class MashbotHandlerChain implements HandlerChain {

	public MashbotHandlerChain(List<ChainableHandler> chain){
		for(int i = 0; i < chain.size()-1; i++){
			chain.get(i).setNext(chain.get(i+1));
		}
		if(chain.size() > 0){
			this.first = chain.get(0);
		}
	}
	
	public void enact(Request request, Response response,
			RequestContext context) throws MashbotException {
		first.enact(request, response, context);
	}

	private ChainableHandler first;

	protected ChainableHandler getFirst() {
		return first;
	}

	protected void setFirst(ChainableHandler first) {
		this.first = first;
	}
}
