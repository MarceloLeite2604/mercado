package org.marceloleite.mercado.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.marceloleite.mercado.CurrencyAmount;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.model.xmladapter.CurrencyXmlAdapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@Table(name = "BALANCES")
@JsonIgnoreProperties({ "id", "account" })
@JsonPropertyOrder({ "currency", "amount" })
@XmlRootElement(name = "balance")
@XmlType(propOrder = { "currency", "amount" })
public class Balance {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ACCO_ID", nullable = false, foreignKey = @ForeignKey(name = "BALA_ACCO_FK"))
	private Account account;

	@Column(name = "CURRENCY", nullable = false, length = 4)
	private Currency currency;

	@Column(name = "AMOUNT")
	private BigDecimal amount;

	@XmlTransient
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlTransient
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@XmlElement
	@XmlJavaTypeAdapter(CurrencyXmlAdapter.class)
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@XmlElement
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public CurrencyAmount asCurrencyAmount() {
		return new CurrencyAmount(currency, amount);
	}
	
	@Override
	public String toString() {
		return asCurrencyAmount().toString();
	}
}