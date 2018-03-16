package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the ORDERS database table.
 * 
 */
@Embeddable
public class OrderIdPO implements Serializable {

	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "ACCO_OWNER", length = 64, insertable = false, updatable = false)
	private String accoOwner;

	@Column(name = "ID")
	private BigDecimal id;

	public OrderIdPO() {
		super();
	}

	public String getAccoOwner() {
		return accoOwner;
	}

	public void setAccoOwner(String accoOwner) {
		this.accoOwner = accoOwner;
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
		OrderIdPO other = (OrderIdPO) obj;
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
		return true;
	}

}
