package org.mashbot.server.handlers;

import org.mashbot.server.handlers.ChainableHandler;
import org.mashbot.server.types.Request;
import org.mashbot.server.types.RequestContext;
import org.mashbot.server.types.Response;
import org.springframework.context.support.AbstractApplicationContext;

public class TheStoryHandler extends ChainableHandler {

	public void postRequest(Request request, Response response,
			RequestContext context) {
		System.out.println("The Story");
	}

	public void preRequest(Request request, Response response,
			RequestContext context) {
		System.out.println("It is Awesome");
	}
	
}
