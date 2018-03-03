package org.marceloleite.mercado.xml.structures;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "tapiInformations")
@XmlType(propOrder= {"id", "secret"})
public class XmlTapiInformation {

	private String id;

	private String secret;

	public XmlTapiInformation() {
		super();
	}

	@XmlElement(name="id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement(name="secret")
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

}
