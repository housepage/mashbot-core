package org.mashbot.server.types;

import org.mashbot.server.types.Request.Field;

public class Response extends GenericFieldStorage {
	public enum Field{
		MOBJECT("mobject");
		Field(String label){
			this.label = label;
		}
		private String label;
	}
	
	public Response(){
		super();
	}
	
	public Object getField(Field key){
		return context.get(key);
	}
	
	public void putField(Field key,Object value){
		context.put(key.toString(), value);
	}

	public void setMObject(MObject object) {
		putField(Field.MOBJECT,object);
	}
	
	public MObject getMObject() {
		return (MObject) getField(Field.MOBJECT);
	}
}
