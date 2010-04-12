package org.mashbot.server.types;

public class Request extends GenericFieldStorage {
	public enum Field {
		OPERATION("operation"),
		CONTENTTYPE("contentType"), 
		MOBJECT("mobject");
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

	public MObject getMObject() {
		return (MObject) getField(Field.MOBJECT);
	}
	
	public void setMObject(MObject incoming) {
		putField(Field.MOBJECT, incoming);
	}
}

