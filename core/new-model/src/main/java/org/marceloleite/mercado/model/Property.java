package org.marceloleite.mercado.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@Table(name = "PROPERTIES")
@JsonIgnoreProperties({ "id", "required", "defaultValue", "encrypted"  })
@JsonPropertyOrder({ "name", "value"})
@XmlRootElement(name = "property")
@XmlType(propOrder = { "name", "value" })
public class Property implements org.marceloleite.mercado.commons.properties.Property {
	
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name="NAME", nullable = false, length=64)
	private String name;

	@Column(name="VALUE", nullable = false, length=128)
	private String value;

	@XmlTransient
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String getDefaultValue() {
		return this.value;
	}

	@Override
	public boolean isRequired() {
		return true;
	}

	@Override
	public boolean isEncrypted() {
		return false;
	}
}