package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.marceloleite.mercado.commons.Currency;

/**
 * The primary key class for the "VARIABLES" database table.
 * 
 */
@Embeddable
public class VariableIdPO implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="CLASS_STRA_ACCO_OWNER", insertable=false, updatable=false)
	private String classStraAccoOwner;

	@Column(name="CLASS_STRA_CURRENCY", insertable=false, updatable=false)
	private Currency classStraCurrency;

	@Column(name="CLASS_NAME", insertable=false, updatable=false)
	private String className;

	private String name;

	public VariableIdPO() {
	}
	public String getClassStraAccoOwner() {
		return this.classStraAccoOwner;
	}
	public void setClassStraAccoOwner(String classStraAccoOwner) {
		this.classStraAccoOwner = classStraAccoOwner;
	}
	public Currency getClassStraCurrency() {
		return this.classStraCurrency;
	}
	public void setClassStraCurrency(Currency classStraCurrency) {
		this.classStraCurrency = classStraCurrency;
	}
	public String getClassName() {
		return this.className;
	}
	public void setClassName(String className) {
		this.className = className;
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
		if (!(other instanceof VariableIdPO)) {
			return false;
		}
		VariableIdPO castOther = (VariableIdPO)other;
		return 
			this.classStraAccoOwner.equals(castOther.classStraAccoOwner)
			&& this.classStraCurrency.equals(castOther.classStraCurrency)
			&& this.className.equals(castOther.className)
			&& this.name.equals(castOther.name);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.classStraAccoOwner.hashCode();
		hash = hash * prime + this.classStraCurrency.hashCode();
		hash = hash * prime + this.className.hashCode();
		hash = hash * prime + this.name.hashCode();
		
		return hash;
	}
}