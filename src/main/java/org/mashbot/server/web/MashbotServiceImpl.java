package org.mashbot.server.web;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;

import org.mashbot.server.handlers.ChainableHandler;
import org.mashbot.server.handlers.MashbotHandlerChain;
import org.mashbot.server.types.Request;
import org.mashbot.server.types.RequestContext;

@Path("/")
public class MashbotServiceImpl implements MashbotService {
	private MashbotHandlerChain handlerChain;

	public MashbotServiceImpl(List<ChainableHandler> handlers){
		this.handlerChain = new MashbotHandlerChain(handlers);
	}
	
	@GET
	@Path("/{operation}/{contentType}")
	@Produces({"text/html"})
	public String dataRequest(@PathParam("operation") String operation, @PathParam("contentType") String contentType,@QueryParam("actor") String actor){
		Request request = new Request();
		request.putField(Request.Field.OPERATION, operation);
		request.putField(Request.Field.CONTENTTYPE, contentType);
		
		this.handlerChain.enact(request,null,null);
		return actor + " " + operation + " " + contentType;
	}	
}
