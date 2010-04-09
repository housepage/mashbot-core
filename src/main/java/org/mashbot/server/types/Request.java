package org.mashbot.server.types;

public class Request extends GenericFieldStorage {
	public enum Field {
		OPERATION("operation"),
		CONTENTTYPE("contentType");
		Field(String label){
			this.label = label;
		}
		private String label;
	}
	
	public Request(){
		super();
	}
}
