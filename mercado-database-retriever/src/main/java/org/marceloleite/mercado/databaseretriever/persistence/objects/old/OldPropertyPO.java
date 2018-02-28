package org.marceloleite.mercado.databaseretriever.persistence.objects.old;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.marceloleite.mercado.databaseretriever.persistence.objects.PersistenceObject;

@Entity(name = "PROPERTIES")
public class OldPropertyPO implements PersistenceObject<String> {

	@Id
	@Column(name="NAME", nullable = false)
	private String name;

	@Column(name="VALUE", nullable = false)
	private String value;

	public OldPropertyPO(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public OldPropertyPO() {
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
		return OldPropertyPO.class;
	}

	@Override
	public String getId() {
		return this.name;
	}
}
