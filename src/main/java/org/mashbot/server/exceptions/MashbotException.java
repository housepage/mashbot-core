package org.mashbot.server.exceptions;

public class MashbotException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MashbotException(){
		super();
	}
	
	public MashbotException(String errorString){
		super(errorString);
	}
}
