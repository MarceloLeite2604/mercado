package org.marceloleite.mercado.databasemodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "PROPERTIES")
public class PropertyPO implements PersistenceObject<String> {

	@Id
	@Column(name="NAME", nullable = false)
	private String name;

	@Column(name="VALUE", nullable = false)
	private String value;

	public PropertyPO(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public PropertyPO() {
		this(null, null);
	}

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
