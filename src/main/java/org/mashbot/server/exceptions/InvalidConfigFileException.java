package org.mashbot.server.exceptions;


public class InvalidConfigFileException extends MashbotException {

	private String configfile;
	
	public InvalidConfigFileException(String configfile) {
		this.configfile = configfile;
	}

}
