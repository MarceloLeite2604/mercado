package org.marceloleite.mercado.api.negotiation.methods.getwithdrawal.response;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.api.negotiation.serializer.WithdrawalStatusDeserializer;
import org.marceloleite.mercado.api.negotiation.serializer.WithdrawalStatusSerializer;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.formatter.ZonedDateTimeSerializer;
import org.marceloleite.mercado.commons.json.deserializer.CurrencyDeserializer;
import org.marceloleite.mercado.commons.json.deserializer.ZonedDateTimeDeserializer;
import org.marceloleite.mercado.commons.json.serializer.CurrencySerializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Withdrawal {

	private Long id;
	
	@JsonProperty("coin")
	@JsonSerialize(using = CurrencySerializer.class)
	@JsonDeserialize(using = CurrencyDeserializer.class)
	private Currency currency;
	
	private BigDecimal quantity;
	
	private BigDecimal netQuantity;
	
	private String account;
	
	private BigDecimal fee;
	
	private String address;
	
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
	
	

	public Withdrawal(long id, Currency currency, double quantity, double net_quantity, String account, double fee,
			String address, WithdrawalStatus status, String description, ZonedDateTime created, ZonedDateTime updated) {
		super();
		this.id = id;
		this.currency = currency;
		this.quantity = quantity;
		this.netQuantity = net_quantity;
		this.account = account;
		this.fee = fee;
		this.address = address;
		this.status = status;
		this.description = description;
		this.created = created;
		this.updated = updated;
	}
	
	public Withdrawal() {
		this(0l, null, 0.0, 0.0, null, 0.0, null, null, null, null, null);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getNetQuantity() {
		return netQuantity;
	}

	public void setNetQuantity(double net_quantity) {
		this.netQuantity = net_quantity;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
}
