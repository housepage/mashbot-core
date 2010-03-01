package com.raulraja.ws;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Hello world object that demonstrates the service
 */
@XmlRootElement
public class HelloWorld {

	/**
	 * the message
	 */
	private String message;

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message sets the message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
