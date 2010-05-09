package org.mashbot.server.exceptions;

import com.aetrion.flickr.FlickrException;

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

	public MashbotException(FlickrException e) {
		super(e);
	}
}
