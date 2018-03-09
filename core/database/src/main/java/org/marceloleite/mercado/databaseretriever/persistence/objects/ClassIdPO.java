package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.marceloleite.mercado.commons.Currency;

/**
 * The primary key class for the CLASSES database table.
 * 
 */
@Embeddable
public class ClassIdPO implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="STRA_ACCO_OWNER", insertable=false, updatable=false)
	private String straAccoOwner;

	@Column(name="STRA_CURRENCY", insertable=false, updatable=false)
	private Currency straCurrency;

	private String name;

	public ClassIdPO() {
	}
	public String getStraAccoOwner() {
		return this.straAccoOwner;
	}
	public void setStraAccoOwner(String straAccoOwner) {
		this.straAccoOwner = straAccoOwner;
	}
	public Currency getStraCurrency() {
		return this.straCurrency;
	}
	public void setStraCurrency(Currency straCurrency) {
		this.straCurrency = straCurrency;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ClassIdPO)) {
			return false;
		}
		ClassIdPO castOther = (ClassIdPO)other;
		return 
			this.straAccoOwner.equals(castOther.straAccoOwner)
			&& this.straCurrency.equals(castOther.straCurrency)
			&& this.name.equals(castOther.name);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.straAccoOwner.hashCode();
		hash = hash * prime + this.straCurrency.hashCode();
		hash = hash * prime + this.name.hashCode();
		
		return hash;
	}
}