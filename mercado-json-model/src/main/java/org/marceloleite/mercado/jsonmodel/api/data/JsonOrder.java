package org.marceloleite.mercado.jsonmodel.api.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"order_id",
"coin_pair",
"order_type",
"status",
"has_fills",
"quantity",
"limit_price",
"executed_quantity",
"executed_price_avg",
"fee",
"created_timestamp",
"updated_timestamp",
"operations"
})
public class JsonOrder {

@JsonProperty("order_id")
private Long orderId;
@JsonProperty("coin_pair")
private String coinPair;
@JsonProperty("order_type")
private Long orderType;
@JsonProperty("status")
private Long status;
@JsonProperty("has_fills")
private Boolean hasFills;
@JsonProperty("quantity")
private String quantity;
@JsonProperty("limit_price")
private String limitPrice;
@JsonProperty("executed_quantity")
private String executedQuantity;
@JsonProperty("executed_price_avg")
private String executedPriceAvg;
@JsonProperty("fee")
private String fee;
@JsonProperty("created_timestamp")
private String createdTimestamp;
@JsonProperty("updated_timestamp")
private String updatedTimestamp;
@JsonProperty("operations")
private List<JsonOperation> operations = null;
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

@JsonProperty("coin_pair")
public String getCoinPair() {
return coinPair;
}

@JsonProperty("coin_pair")
public void setCoinPair(String coinPair) {
this.coinPair = coinPair;
}

@JsonProperty("order_type")
public Long getOrderType() {
return orderType;
}

@JsonProperty("order_type")
public void setOrderType(Long orderType) {
this.orderType = orderType;
}

@JsonProperty("status")
public Long getStatus() {
return status;
}

@JsonProperty("status")
public void setStatus(Long status) {
this.status = status;
}

@JsonProperty("has_fills")
public Boolean getHasFills() {
return hasFills;
}

@JsonProperty("has_fills")
public void setHasFills(Boolean hasFills) {
this.hasFills = hasFills;
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

@JsonProperty("executed_quantity")
public String getExecutedQuantity() {
return executedQuantity;
}

@JsonProperty("executed_quantity")
public void setExecutedQuantity(String executedQuantity) {
this.executedQuantity = executedQuantity;
}

@JsonProperty("executed_price_avg")
public String getExecutedPriceAvg() {
return executedPriceAvg;
}

@JsonProperty("executed_price_avg")
public void setExecutedPriceAvg(String executedPriceAvg) {
this.executedPriceAvg = executedPriceAvg;
}

@JsonProperty("fee")
public String getFee() {
return fee;
}

@JsonProperty("fee")
public void setFee(String fee) {
this.fee = fee;
}

@JsonProperty("created_timestamp")
public String getCreatedTimestamp() {
return createdTimestamp;
}

@JsonProperty("created_timestamp")
public void setCreatedTimestamp(String createdTimestamp) {
this.createdTimestamp = createdTimestamp;
}

@JsonProperty("updated_timestamp")
public String getUpdatedTimestamp() {
return updatedTimestamp;
}

@JsonProperty("updated_timestamp")
public void setUpdatedTimestamp(String updatedTimestamp) {
this.updatedTimestamp = updatedTimestamp;
}

@JsonProperty("operations")
public List<JsonOperation> getOperations() {
return operations;
}

@JsonProperty("operations")
public void setOperations(List<JsonOperation> operations) {
this.operations = operations;
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