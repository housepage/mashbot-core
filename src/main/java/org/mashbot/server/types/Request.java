package org.mashbot.server.types;

import java.util.UUID;

import org.mashbot.server.types.MObject.Field;
import org.mashbot.server.web.MashbotService;
import org.mashbot.server.web.MashbotService.Operation;

public class Request extends GenericFieldStorage {
	public enum Field {
		OPERATION("operation"),
		CONTENTTYPE("contentType"), 
		MOBJECT("mobject"), 
		ID("id"), 
		TOKEN("token");
		Field(String label){
			this.label = label;
		}
		public String toString(){
			return this.label;
		}
		private String label;
	}
	
	public Request(){
		super();
	}
	
	public Object getField(Field key){
		return super.getField(key.toString());
	}
	
	public void putField(Field key,Object value){
		super.putField(key.toString(), value);
	}
	
	public Object getField(Field key, String service){
		return this.getField(key.toString());
	}
	
	public void putField(Field key,String service,Object value){
		this.putField(GenericFieldStorage.join(key.toString(),service), value);
	}

	public MObject getMObject() {
		return (MObject) getField(Field.MOBJECT);
	}
	
	public void setMObject(MObject incoming) {
		putField(Field.MOBJECT, incoming);
	}
	
	public String getOperation() {
		return getField(Field.OPERATION).toString();
	}
	
	public void setOperation(Operation operation) {
		putField(Field.OPERATION, operation.toString());
	}
	
	public void setOperation(String operation) {
		putField(Field.OPERATION, operation);
	}
	
	public String getContentType() {
		return (String) getField(Field.CONTENTTYPE);
	}
	
	public void setContentType(String incoming) {
		putField(Field.CONTENTTYPE, incoming);
	}

	public UUID getToken() {
		return (UUID) getField(Field.TOKEN);
	}
	
	public void setToken(UUID token){
		putField(Field.TOKEN,token);
	}
}

