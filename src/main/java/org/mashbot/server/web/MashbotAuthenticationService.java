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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.mashbot.server.types.ServiceCredential;

@Path("auth")
public class MashbotAuthenticationService {
	@GET
	@Produces("application/json")
	public String listAuthenticationInformation(@QueryParam("token") String token){
		return "";
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Map<String,ServiceCredential> getAuthenticationToken(Map<String,ServiceCredential> credentials){
		return new HashMap<String,ServiceCredential>();
	}
	
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Map<String,ServiceCredential> addAuthenticationInformation(@QueryParam("token") String token, Map<String,ServiceCredential> credentials){
		return new HashMap<String,ServiceCredential>();
	}
	
	@DELETE
	@Produces("application/json")
	public boolean invalidateAuthenticationToken(){
		return true;
	}
}
