package org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class JsonCurrencyAvailable {

	@JsonProperty("available")
	private Double avaliable;

	@JsonProperty("total")
	private Double total;

	@JsonProperty("amount_open_orders")
	private Long amountOpenOrders;

	public JsonCurrencyAvailable(Double avaliable, Double total, Long amountOpenOrders) {
		super();
		this.avaliable = avaliable;
		this.total = total;
		this.amountOpenOrders = amountOpenOrders;
	}

	public JsonCurrencyAvailable() {
		this(0.0, 0.0, 0l);
	}

	@JsonProperty("available")
	public Double getAvaliable() {
		return avaliable;
	}

	@JsonProperty("available")
	public void setAvaliable(Double avaliable) {
		this.avaliable = avaliable;
	}

	@JsonProperty("total")
	public Double getTotal() {
		return total;
	}

	@JsonProperty("total")
	public void setTotal(Double total) {
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
