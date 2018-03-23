package org.marceloleite.mercado.converter.entity;

import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.OperationData;
import org.marceloleite.mercado.databaseretriever.persistence.objects.OperationIdPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.OperationPO;

public class OperationPOToOperationDataConverter implements Converter<OperationPO, OperationData> {

	@Override
	public OperationData convertTo(OperationPO operationPO) {
		OperationData operationData = new OperationData();
		operationData.setId(operationPO.getId().getId().longValue());
		operationData.setExecuted(ZonedDateTime.from(operationPO.getExecuted()));
		operationData.setFeeRate(operationPO.getFeeRate().doubleValue());
		operationData.setPrice(operationPO.getPrice().doubleValue());
		operationData.setQuantity(operationPO.getQuantity().doubleValue());
		return operationData;
	}

	@Override
	public OperationPO convertFrom(OperationData operationData) {
		OperationPO operationPO = new OperationPO();
		OperationIdPO operationIdPO = new OperationIdPO();
		operationIdPO.setId(new MercadoBigDecimal(operationData.getId()));
		operationPO.setId(operationIdPO);
		operationPO.setExecuted(ZonedDateTime.from(operationData.getExecuted()));
		operationPO.setFeeRate(new MercadoBigDecimal(operationData.getFeeRate()));
		operationPO.setPrice(new MercadoBigDecimal(operationData.getPrice()));
		operationPO.setQuantity(new MercadoBigDecimal(operationData.getQuantity()));
		return operationPO;
	}

}
