package org.mashbot.server.web;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;

import org.apache.commons.logging.LogFactory;
import org.mashbot.server.auth.AuthenticationManager;
import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.handlers.ChainableHandler;
import org.mashbot.server.handlers.MashbotHandlerChain;
import org.mashbot.server.plugins.PluginManager;
import org.mashbot.server.types.GenericFieldStorage;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.Request;
import org.mashbot.server.types.Response;
import org.mashbot.server.types.RequestContext;
import org.apache.commons.logging.Log;

import com.sun.corba.se.spi.ior.MakeImmutable;


@Path("{contentType}")
public class MashbotService {
	private MashbotHandlerChain handlerChain;
	private PluginManager pluginMan;
	private AuthenticationManager authMan;
	private Log log = LogFactory.getLog(getClass());

	public enum Operation{
		PULL("pull"), 
		PUSH("push"), 
		EDIT("edit"),
		DELETE("delete");
		Operation(String label){
			this.label = label;
		}
		public String toString(){
			return this.label;
		}
		private String label;
	}

	public MashbotService(List<ChainableHandler> handlers){
		this.handlerChain = new MashbotHandlerChain(handlers);
	}
	
	
	public MObject makeRequest(Operation op, String contentType, String token){
		return makeRequest(op, contentType,new MObject(),token);
	}
	
	public MObject makeRequest(Operation op, String contentType, MObject in){
		return makeRequest(op, contentType, in,null);
	}
	
	public MObject makeRequest(Operation op, String contentType, MObject in, String token){
		Request request = new Request();
		request.setContentType(contentType);
		request.setOperation(op);
		try{
			request.setToken(UUID.fromString(token));
		} catch (NullPointerException e){
			request.setToken(null);
		}
		request.setMObject(in);
		
		Response response = new Response();
		response.setMObject(new MObject());
		
		RequestContext context = new RequestContext();
		log.warn("Plugin Manager: " + this.pluginMan);
		log.warn("Authentication Manager: " + this.authMan);
		context.setPluginManager(this.pluginMan);
		context.setAuthenticationManager(this.authMan);
				
		try {
			this.handlerChain.enact(request, response, context);
		} catch (MashbotException e) {
			e.printStackTrace();
		}
		
		return response.getMObject();
	}
	
	@GET
	@Produces({"application/json"})
	public MObject pullRequest(@PathParam("contentType") String contentType, @QueryParam("token") String token)
	{
		//return makeRequest(Operation.PULL,contentType,token);
		MObject ret = new MObject();
		List<String> status = new ArrayList<String>();
		status.add("Herro you are awesome");
		ret.putField(MObject.Field.STATUS, status);
		List<String> tags = new ArrayList<String>();
		tags.add("#jobs");
		tags.add("#boom");
		ret.putField(MObject.Field.TAGS,tags);
		System.out.println("Exiting");
		System.out.flush();
		return ret;
	}	
	
	@POST
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public MObject pushRequest(@PathParam("contentType") String contentType, @QueryParam("token") String token, MObject incoming)
	{
		System.out.println("token:"+token+" "+UUID.fromString(token));
		return makeRequest(Operation.PUSH,contentType,incoming,token);
	}
	
	@PUT
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public MObject editRequest(@PathParam("contentType") String contentType, @QueryParam("token") String token, MObject incoming)
	{
		return makeRequest(Operation.EDIT,contentType,incoming);
	}
	
	@DELETE
	@Produces({"application/json"})
	@Path("{service}/{id}")
	public MObject deleteRequest(@PathParam("contentType") String contentType,@PathParam("service") String service,@PathParam("id") String id, @QueryParam("token") String token){
		MObject delete = new MObject();
		//delete.putField(GenericFieldStorage.join(Request.Field.ID,service), id);
		
		return makeRequest(Operation.DELETE,contentType,delete);
	}
	
	public PluginManager getPluginMan() {
		return pluginMan;
	}

	public void setPluginMan(PluginManager pluginMan) {
		this.pluginMan = pluginMan;
	}

	public AuthenticationManager getAuthMan() {
		return authMan;
	}

	public void setAuthMan(AuthenticationManager authMan) {
		this.authMan = authMan;
	}
}
