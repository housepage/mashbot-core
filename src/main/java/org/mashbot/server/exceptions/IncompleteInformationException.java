package org.mashbot.server.exceptions;

import java.util.List;
import java.util.Map;



public class IncompleteInformationException extends MashbotException{
	
	public Map<String, List<String>> requiredFields;
	
	public IncompleteInformationException(Map<String, List<String>> requiredFields){
		super();
		this.requiredFields = requiredFields;
	}

	private static final long serialVersionUID = 1L;

}
