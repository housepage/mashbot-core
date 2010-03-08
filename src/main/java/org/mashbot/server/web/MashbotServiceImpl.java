package org.mashbot.server.web;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;

@Path("/v1/")
public class MashbotServiceImpl implements MashbotService {
	@GET
	@Path("/echo")
	@Produces({"text/html"})
	public String echo(){
		return "Hello";
	} 
}
