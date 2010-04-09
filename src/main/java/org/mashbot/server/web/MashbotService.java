package org.mashbot.server.web;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;

import org.mashbot.server.handlers.ChainableHandler;
import org.mashbot.server.handlers.MashbotHandlerChain;
import org.mashbot.server.types.MObject;

@Path("{contentType}")
public class MashbotService {
	private MashbotHandlerChain handlerChain;

	public MashbotService(List<ChainableHandler> handlers){
		this.handlerChain = new MashbotHandlerChain(handlers);
	}
	
	@GET
	@Produces({"application/json"})
	public MObject pullRequest(@PathParam("contentType") String contentType){
		this.handlerChain.enact(null,null,null);
		return new MObject();
	}	
	
	@POST
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public MObject pushRequest(@PathParam("contentType") String contentType, MObject incoming)
	{
		return incoming;	
	}
	
	@PUT
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public MObject editRequest(@PathParam("contentType") String contentType, MObject incoming)
	{
		return incoming;	
	}
	
	@DELETE
	@Produces({"application/json"})
	@Path("{service}/{id}")
	public MObject deleteRequest(@PathParam("contentType") String contentType,@PathParam("service") String service,@PathParam("id") String id){
		return new MObject();
	}
}
