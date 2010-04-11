package org.mashbot.server.web;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("auth")
public class MashbotAuthenticationService {
	@GET
	@Produces("application/json")
	public Object listAuthenticationInformation(){
		return new HashMap<String,String>();
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Map<String,String> getAuthenticationToken(){
		return new HashMap<String,String>();
	}
	
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Map<String,String> addAuthenticationInformation(){
		return new HashMap<String,String>();
	}
	
	@DELETE
	@Produces("application/json")
	public Map<String,String> invalidateAuthenticationToken(){
		return new HashMap<String,String>();
	}
	
	private Map<String,>
}
