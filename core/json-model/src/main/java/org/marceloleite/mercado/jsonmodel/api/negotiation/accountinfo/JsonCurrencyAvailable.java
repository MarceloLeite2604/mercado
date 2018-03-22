package org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class JsonCurrencyAvailable {

	@JsonProperty("available")
	private BigDecimal avaliable;

	@JsonProperty("total")
	private BigDecimal total;

	@JsonProperty("amount_open_orders")
	private Long amountOpenOrders;

	public JsonCurrencyAvailable(BigDecimal avaliable, BigDecimal total, Long amountOpenOrders) {
		super();
		this.avaliable = avaliable;
		this.total = total;
		this.amountOpenOrders = amountOpenOrders;
	}

	public JsonCurrencyAvailable() {
		this(new BigDecimal("0.0"), new BigDecimal("0.0"), 0l);
	}

	@JsonProperty("available")
	public BigDecimal getAvaliable() {
		return avaliable;
	}

	@JsonProperty("available")
	public void setAvaliable(BigDecimal avaliable) {
		this.avaliable = avaliable;
	}

	@JsonProperty("total")
	public BigDecimal getTotal() {
		return total;
	}

	@JsonProperty("total")
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	@JsonProperty("amount_open_orders")
	public Long getAmountOpenOrders() {
		return amountOpenOrders;
	}

	@JsonProperty("amount_open_orders")
	public void setAmountOpenOrders(Long amountOpenOrders) {
		this.amountOpenOrders = amountOpenOrders;
	}

}
