package org.marceloleite.mercado.databasemodel;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="Properties")
public class PropertyPO implements PersistenceObject<String> {

	@Id
	private String name;
	
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
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
		return this.name;
	}
}