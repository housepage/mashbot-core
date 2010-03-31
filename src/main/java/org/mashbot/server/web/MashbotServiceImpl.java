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
		this.handlerChain.enact(null,null,null);
		return actor + " " + operation + " " + contentType;
	}	
}
