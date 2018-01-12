package org.marceloleite.mercado.jsonmodel;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "operation_id", "quantity", "price", "fee_rate", "executed_timestamp" })
public class JsonOperation {

	@JsonProperty("operation_id")
	private Long operationId;
	
	@JsonProperty("quantity")
	private String quantity;
	
	@JsonProperty("price")
	private String price;
	
	@JsonProperty("fee_rate")
	private String feeRate;
	
	@JsonProperty("executed_timestamp")
	private String executedTimestamp;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("operation_id")
	public Long getOperationId() {
		return operationId;
	}

	@JsonProperty("operation_id")
	public void setOperationId(Long operationId) {
		this.operationId = operationId;
	}

	@JsonProperty("quantity")
	public String getQuantity() {
		return quantity;
	}

	@JsonProperty("quantity")
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	@JsonProperty("price")
	public String getPrice() {
		return price;
	}

	@JsonProperty("price")
	public void setPrice(String price) {
		this.price = price;
	}

	@JsonProperty("fee_rate")
	public String getFeeRate() {
		return feeRate;
	}

	@JsonProperty("fee_rate")
	public void setFeeRate(String feeRate) {
		this.feeRate = feeRate;
	}

	@JsonProperty("executed_timestamp")
	public String getExecutedTimestamp() {
		return executedTimestamp;
	}

	@JsonProperty("executed_timestamp")
	public void setExecutedTimestamp(String executedTimestamp) {
		this.executedTimestamp = executedTimestamp;
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
