package org.mashbot.server.types;

import org.mashbot.server.types.MObject.Field;
import org.mashbot.server.web.MashbotService;
import org.mashbot.server.web.MashbotService.Operation;

public class Request extends GenericFieldStorage {
	public enum Field {
		OPERATION("operation"),
		CONTENTTYPE("contentType"), 
		MOBJECT("mobject"), 
		ID("id");
		Field(String label){
			this.label = label;
		}
		private String label;
	}
	
	public Request(){
		super();
	}
	
	public Object getField(Field key){
		return context.get(key);
	}
	
	public void putField(Field key,Object value){
		context.put(key.toString(), value);
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
	
	public MashbotService.Operation getOperation() {
		return (MashbotService.Operation) getField(Field.OPERATION);
	}
	
	public void setOperation(Operation operation) {
		putField(Field.OPERATION, operation);
	}
	
	public String getContentType() {
		return (String) getField(Field.CONTENTTYPE);
	}
	
	public void setContentType(String incoming) {
		putField(Field.CONTENTTYPE, incoming);
	}
}

