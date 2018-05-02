package org.marceloleite.mercado.api.negotiation.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.api.negotiation.model.deserializer.WithdrawalStatusDeserializer;
import org.marceloleite.mercado.api.negotiation.model.serializer.WithdrawalStatusSerializer;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.json.deserializer.CurrencyDeserializer;
import org.marceloleite.mercado.commons.json.deserializer.ZonedDateTimeDeserializer;
import org.marceloleite.mercado.commons.json.serializer.CurrencySerializer;
import org.marceloleite.mercado.commons.json.serializer.ZonedDateTimeSerializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Withdrawal {

	private Long id;
	
	@JsonProperty("coin")
	@JsonSerialize(using = CurrencySerializer.class)
	@JsonDeserialize(using = CurrencyDeserializer.class)
	private Currency currency;
	
	private BigDecimal fee;
	
	@JsonProperty("status")
	@JsonSerialize(using = WithdrawalStatusSerializer.class)
	@JsonDeserialize(using = WithdrawalStatusDeserializer.class)
	private WithdrawalStatus status;
	
	private String description;
	
	@JsonProperty("created_timestamp")
	@JsonSerialize(using = ZonedDateTimeSerializer.class)
	@JsonDeserialize(using = ZonedDateTimeDeserializer.class)
	private ZonedDateTime created;
	
	@JsonProperty("updated_timestamp")
	@JsonSerialize(using = ZonedDateTimeSerializer.class)
	@JsonDeserialize(using = ZonedDateTimeDeserializer.class)
	private ZonedDateTime updated;
	
	private BigDecimal quantity;
	
	@JsonProperty("net_quantity")
	private BigDecimal netQuantity;
	
	private String account;
	
	private String address;
	
	private String tx;

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

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public WithdrawalStatus getStatus() {
		return status;
	}

	public void setStatus(WithdrawalStatus status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ZonedDateTime getCreated() {
		return created;
	}

	public void setCreated(ZonedDateTime created) {
		this.created = created;
	}

	public ZonedDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(ZonedDateTime updated) {
		this.updated = updated;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getNetQuantity() {
		return netQuantity;
	}

	public void setNetQuantity(BigDecimal netQuantity) {
		this.netQuantity = netQuantity;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTx() {
		return tx;
	}

	public void setTx(String tx) {
		this.tx = tx;
	}		
}
