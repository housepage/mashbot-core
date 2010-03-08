package org.mashbot.server.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;

@Path("/rs")
public class MashbotServiceImpl implements MashbotService {
	@GET
	@Path("/echo/{operation}/{contentType}")
	@Produces({"text/xml"})
	public String echo(@PathParam("operation") String operation, @PathParam("contentType") String contentType){
		return operation + " " + contentType;
	} 
}
