package org.marceloleite.mercado.xml.structures;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "strategy")
public class XmlClass {
	
	private String name;
	
	private List<XmlParameter> xmlParameters;
	
	public XmlClass(String name) {
		super();
		this.name = name;
	}

	public XmlClass() {
		this(null);
	}

	@XmlElement(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElementWrapper(name="parameters")
	@XmlElement(name="parameter")
	public List<XmlParameter> getXmlParameters() {
		return xmlParameters;
	}

	public void setXmlParameters(List<XmlParameter> xmlParameters) {
		this.xmlParameters = xmlParameters;
	}	
}
