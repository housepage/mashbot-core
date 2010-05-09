package org.mashbot.server.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

import org.apache.abdera.ext.json.JSONUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.common.util.Base64Exception;
import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.jaxrs.provider.JSONUtils;
import org.mashbot.server.auth.AuthenticationManager;
import org.mashbot.server.types.GenericFieldStorage;
import org.mashbot.server.types.MashbotReturn;
import org.mashbot.server.types.ServiceCredential;
import org.mashbot.server.types.UserAuthenticationInformation;
import org.mashbot.server.xml.MapXmlAdapter;
import org.mashbot.server.xml.XmlMap;
import org.mortbay.jetty.security.Credential;

@Path("auth")
public class MashbotAuthenticationService {
	
	private Log log = LogFactory.getLog(getClass());

	@GET
	@Produces("application/json")
	public UserAuthenticationInformation listAuthenticationInformation(@QueryParam("token") String token){
		/*UserAuthenticationInformation authInfo = new UserAuthenticationInformation();
		ServiceCredential twitter = new ServiceCredential();
		twitter.putField("username","yomama");
		authInfo.addCredential("twitter",twitter);
		return authInfo; */
		//return authman.listAuthenticationInformation(UUID.fromString(token));
		try {
			/*UserAuthenticationInformation a = new UserAuthenticationInformation();
			ArrayList<ServiceCredential> credentials = new ArrayList<ServiceCredential>();
			ServiceCredential basic = new ServiceCredential();
			basic.key = "a";
			basic.secret = "b";
			basic.method = "userpass";
			credentials.add(basic);
			credentials.add(basic);
			a.addCredential("twitter", credentials);
			a.addCredential("tumblr", credentials);
			log.warn(a.getCredentials());
			return a;*/
			return authman.listAuthenticationInformation(UUID.fromString(token));
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}
	}
	
	@POST
	@Consumes("application/json; charset=UTF-8;")
	@Produces("application/json; charset=UTF-8;")
	/*@XmlJavaTypeAdapter(value=org.mashbot.server.xml.MapXmlAdapter.class,type=java.util.Map.class)*/
	public String getAuthenticationToken(UserAuthenticationInformation authInfo, @Context HttpHeaders headers){
		return this.authman.getAuthenticationToken(getUser(headers),authInfo.getCredentials());
	}
	
	@PUT
	@Consumes("application/json; charset=UTF-8;")
	@Produces("application/json; charset=UTF-8;")
	public UserAuthenticationInformation addAuthenticationInformation(@QueryParam("token") String token, UserAuthenticationInformation authInfo){
		return authman.updateAuthenticationCredentials(UUID.fromString(token), authInfo.getCredentials());
	}
	
	@DELETE
	@Produces("application/json; charset=UTF-8;")
	public boolean invalidateAuthenticationToken(@QueryParam("token") String token, @Context HttpHeaders headers){
		if(token.equals("")){
			return authman.invalidateAllUserAuthenticationToken(getUser(headers));
		} else {
			return authman.invalidateAuthenticationToken(UUID.fromString(token));
		}
	}
	/**
	 * @author Arul Dhesiaseelan
	 * {@link http://aruld.info/accessing-restful-services-configured-with-ssl-using-resttemplate/}
	 * @param headers
	 * @return
	 */
	private String getUser(HttpHeaders headers) { 
        String auth = headers.getRequestHeader("authorization").get(0); 
 
        auth = auth.substring("Basic ".length()); 
        String[] values;
		try {
			values = new String(Base64Utility.decode(auth)).split(":");
		} catch (Base64Exception e) {
			return null;
		} 
 
        String username = values[0]; 
        String password = values[1]; 
 
        return username; 
    } 
	
	private AuthenticationManager authman;

	public AuthenticationManager getAuthman() {
		return authman;
	}

	public void setAuthman(AuthenticationManager authman) {
		this.authman = authman;
	}
}
