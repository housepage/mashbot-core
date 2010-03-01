package com.raulraja.ws.impl;

import com.raulraja.ws.HelloWorld;
import com.raulraja.ws.HelloWorldService;

/**
 * Implementation of the hello world web service.
 */
public class HelloWorldServiceImpl implements HelloWorldService {

	/**
	 * Simple methods that says hello
	 *
	 * @return hello world rest and soap!!!
	 */
	public HelloWorld sayHello() {
		HelloWorld helloWorld = new HelloWorld();
		helloWorld.setMessage("Hello world rest and soap!!!");
		return helloWorld;
	}
}
