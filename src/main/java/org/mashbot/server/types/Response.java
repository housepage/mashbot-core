package org.mashbot.server.types;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mashbot.server.types.Request.Field;

public class Response extends GenericFieldStorage {
	public enum Field{
		MOBJECT("mobject");
		Field(String label){
			this.label = label;
		}
		private String label;
	}

	private Log log = LogFactory.getLog(getClass());
	
	public Response(){
		super();
	}
	
	public Object getField(Field key){
		return this.getField(key.toString().toLowerCase());
	}
	
	public void putField(Field key,Object value){
		this.putField(key.toString().toLowerCase(), value);
	}

	public void setMObject(MObject object) {
		putField(Field.MOBJECT,object);
	}
	
	public MObject getMObject() {
		log.warn(getField(Field.MOBJECT));
		return (MObject) getField(Field.MOBJECT);
	}
}
