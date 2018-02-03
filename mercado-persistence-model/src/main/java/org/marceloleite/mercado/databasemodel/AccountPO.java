package org.marceloleite.mercado.databasemodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="ACCOUNTS")
public class AccountPO implements PersistenceObject<String> {

	@Id
	@Column(name="OWNER", nullable = false)
	private String owner;

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public Class<?> getEntityClass() {
		return AccountPO.class;
	}

	@Override
	public String getId() {
		return owner;
	}	
}
