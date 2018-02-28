package org.marceloleite.mercado.databaseretriever.persistence.objects.old;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.marceloleite.mercado.commons.Currency;

@Embeddable
public class OldTradeIdPO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="TRADE_ID", nullable = false)
	private Long id;
	
	@Column(name="CURRENCY", nullable = false)
	private Currency currency;

	public OldTradeIdPO() {
		super();
	}

	public OldTradeIdPO(Long id, Currency currency) {
		super();
		this.id = id;
		this.currency = currency;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
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
		OldTradeIdPO other = (OldTradeIdPO) obj;
		if (currency != other.currency)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
