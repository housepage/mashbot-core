package org.mashbot.server.types;

public class Response extends GenericFieldStorage {
	public enum Field{
		NULL("null");
		Field(String label){
			this.label = label;
		}
		private String label;
	}
	
	public Response(){
		super();
	}
}
