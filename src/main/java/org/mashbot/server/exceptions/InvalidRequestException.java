package org.mashbot.server.exceptions;


import java.io.IOException;

import com.aetrion.flickr.FlickrException;
import com.google.gdata.util.ServiceException;

public class InvalidRequestException extends MashbotException {

	public InvalidRequestException(FlickrException e) {
		super(e);
	}

	public InvalidRequestException(IOException e) {
		super(e);
	}

	public InvalidRequestException(ServiceException e) {
		super(e);
	}

}
