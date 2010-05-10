package org.mashbot.server.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttachmentRef;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.apache.cxf.jaxrs.ext.xml.XMLName;
import org.apache.xmlbeans.impl.inst2xsd.util.Element;
import org.mashbot.server.types.ServiceCredential;

@XmlType(name="")
@XmlRootElement(name="")
public class AllServiceCredentials {
	
	public AllServiceCredentials(){
		this.service = "";
		this.credentials = new ArrayList<ServiceCredential>();
	}
	
	@XmlElement
	public String service;
	
	@XmlElement
	public List<ServiceCredential> credentials;
}
