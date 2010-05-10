package org.mashbot.server.exceptions;

import org.mashbot.server.types.MObject;

public class InvalidFieldException extends MashbotException {

	String invalidField;
	
	public InvalidFieldException(String field) {
		this.invalidField = field;
	}
	
	public InvalidFieldException(MObject.Field field) {
		this.invalidField = field.toString();
	}

}
