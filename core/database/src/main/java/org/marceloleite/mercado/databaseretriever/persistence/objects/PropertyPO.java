package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the PROPERTIES database table.
 * 
 */
@Entity
@Table(name="PROPERTIES")
@NamedQuery(name="PropertyPO.findAll", query="SELECT p FROM PropertyPO p")
public class PropertyPO implements Serializable, PersistenceObject<String> {
	private static final long serialVersionUID = 1L;

	@Id
	private String name;

	@Column(name="\"VALUE\"")
	private String value;

	public PropertyPO() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public Class<?> getEntityClass() {
		return PropertyPO.class;
	}

	@Override
	public String getId() {
		return name;
	}
}