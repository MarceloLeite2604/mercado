package org.marceloleite.mercado.negotiationapi.model.getwithdrawal;

import java.time.LocalDateTime;

import org.marceloleite.mercado.commons.Currency;

public class Withdrawal {

	private long id;
	
	private Currency currency;
	
	private double quantity;
	
	private double netQuantity;
	
	private String account;
	
	private double fee;
	
	private String address;
	
	private WithdrawalStatus status;
	
	private String description;
	
	private LocalDateTime created;
	
	private LocalDateTime updated;
	
	

	public Withdrawal(long id, Currency currency, double quantity, double net_quantity, String account, double fee,
			String address, WithdrawalStatus status, String description, LocalDateTime created, LocalDateTime updated) {
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

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}	
}
