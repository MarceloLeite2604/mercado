package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the BALANCES database table.
 * 
 */
@Embeddable
public class OperationIdPO implements Serializable {
	
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "ACCO_OWNER", length = 64, insertable = false, updatable = false)
	private String accoOwner;
	
	@Column(name = "ORDE_ID", insertable = false, updatable = false)
	private BigDecimal ordeId;
	
	@Column(name = "ID")
	private BigDecimal id;

	public OperationIdPO() {
		super();
	}

	public String getAccoOwner() {
		return accoOwner;
	}

	public void setAccoOwner(String accoOwner) {
		this.accoOwner = accoOwner;
	}

	public BigDecimal getOrdeId() {
		return ordeId;
	}

	public void setOrdeId(BigDecimal ordeId) {
		this.ordeId = ordeId;
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accoOwner == null) ? 0 : accoOwner.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((ordeId == null) ? 0 : ordeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OperationIdPO other = (OperationIdPO) obj;
		if (accoOwner == null) {
			if (other.accoOwner != null)
				return false;
		} else if (!accoOwner.equals(other.accoOwner))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (ordeId == null) {
			if (other.ordeId != null)
				return false;
		} else if (!ordeId.equals(other.ordeId))
			return false;
		return true;
	}

}
