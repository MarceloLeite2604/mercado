package org.marceloleite.mercado.jsonmodel.api.negotiation.orderbook;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "order_id", "quantity", "limit_price", "is_owner" })
public class JsonOrderbookRegister {

	@JsonProperty("order_id")
	private Long orderId;
	
	@JsonProperty("quantity")
	private String quantity;
	
	@JsonProperty("limit_price")
	private String limitPrice;
	
	@JsonProperty("is_owner")
	private Boolean isOwner;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("order_id")
	public Long getOrderId() {
		return orderId;
	}

	@JsonProperty("order_id")
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	@JsonProperty("quantity")
	public String getQuantity() {
		return quantity;
	}

	@JsonProperty("quantity")
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	@JsonProperty("limit_price")
	public String getLimitPrice() {
		return limitPrice;
	}

	@JsonProperty("limit_price")
	public void setLimitPrice(String limitPrice) {
		this.limitPrice = limitPrice;
	}

	@JsonProperty("is_owner")
	public Boolean getIsOwner() {
		return isOwner;
	}

	@JsonProperty("is_owner")
	public void setIsOwner(Boolean isOwner) {
		this.isOwner = isOwner;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}