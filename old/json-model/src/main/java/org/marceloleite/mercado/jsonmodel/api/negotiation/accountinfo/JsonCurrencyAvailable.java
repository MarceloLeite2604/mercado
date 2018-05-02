package org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo;

import org.marceloleite.mercado.commons.MercadoBigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class JsonCurrencyAvailable {

	@JsonProperty("available")
	private MercadoBigDecimal avaliable;

	@JsonProperty("total")
	private MercadoBigDecimal total;

	@JsonProperty("amount_open_orders")
	private Long amountOpenOrders;

	public JsonCurrencyAvailable(MercadoBigDecimal avaliable, MercadoBigDecimal total, Long amountOpenOrders) {
		super();
		this.avaliable = avaliable;
		this.total = total;
		this.amountOpenOrders = amountOpenOrders;
	}

	public JsonCurrencyAvailable() {
		this(new MercadoBigDecimal("0.0"), new MercadoBigDecimal("0.0"), 0l);
	}

	@JsonProperty("available")
	public MercadoBigDecimal getAvaliable() {
		return avaliable;
	}

	@JsonProperty("available")
	public void setAvaliable(MercadoBigDecimal avaliable) {
		this.avaliable = avaliable;
	}

	@JsonProperty("total")
	public MercadoBigDecimal getTotal() {
		return total;
	}

	@JsonProperty("total")
	public void setTotal(MercadoBigDecimal total) {
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
