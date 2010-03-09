package org.mashbot.server.web;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;

@Path("/")
public class MashbotServiceImpl implements MashbotService {
	@GET
	@Path("/{operation}/{contentType}")
	@Produces({"text/html"})
	public String dataRequest(@PathParam("operation") String operation, @PathParam("contentType") String contentType,@QueryParam("actor") String actor){
		return actor + " " + operation + " " + contentType;
	}	
}
