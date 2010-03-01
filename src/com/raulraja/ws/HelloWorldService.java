package com.raulraja.ws;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.jws.WebService;
import javax.jws.WebMethod;

/**
 * The hello world service interface exposed as soap and rest
 *
 * @ WebService is for soap
 * @ Path is for the rest top path
 */
@Path("/helloWorldService/")
@WebService(serviceName = "HelloWorld", name = "HelloWorldService", targetNamespace = "http://ws.raulraja.com")
public interface HelloWorldService {

	/**
	 * Simple methods that says hello
	 *
	 * @return hello world rest and soap!!!
	 * @ WebMethod is for soap
	 * @ GET is for REST
	 * @ Path is for the REST service path
	 */
	@WebMethod
	@GET
	@Path("/")
	HelloWorld sayHello();

}
