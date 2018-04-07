package org.marceloleite.mercado.controller.test;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlElement;

import org.marceloleite.mercado.commons.Currency;

/**
 * The primary key class for the "PARAMETERS" database table.
 * 
 */
@Embeddable
public class ParameterId implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="CLASS_STRA_ACCO_OWNER", length=64, insertable=false, updatable=false)
	private String classStraAccoOwner;

	@Column(name="CLASS_STRA_CURRENCY", length=4, insertable=false, updatable=false)
	private Currency classStraCurrency;

	@Column(name="CLASS_NAME", length=128, insertable=false, updatable=false)
	private String className;

	@Column(name="NAME", length=64)
	@XmlElement(name="name")
	private String name;

	public ParameterId() {
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
		if (!(other instanceof ParameterId)) {
			return false;
		}
		ParameterId castOther = (ParameterId)other;
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