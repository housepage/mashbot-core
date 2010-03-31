package org.mashbot.server.handlers;

import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;

import org.mashbot.server.handlers.ChainableHandler;
import org.mashbot.server.types.Request;
import org.mashbot.server.types.RequestContext;
import org.mashbot.server.types.Response;
import org.springframework.context.support.AbstractApplicationContext;

public class GreetingHandler extends ChainableHandler {

	@Override
	public void postRequest(Request request, Response response,
			RequestContext context) {
			System.out.println("Goodbye");
	}

	@Override
	public void preRequest(Request request, Response response,
			RequestContext context) {
			System.out.println("Hello");
	}
	
}
