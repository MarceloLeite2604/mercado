package org.marceloleite.mercado.databaseretriever.persistence.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.marceloleite.mercado.commons.TradeType;

@Converter(autoApply = true)
public class TradeTypeAttributeConverter implements AttributeConverter<TradeType, String> {

	@Override
	public String convertToDatabaseColumn(TradeType tradeType) {
		return (null == tradeType ? null : tradeType.getValue());
	}

	@Override
	public TradeType convertToEntityAttribute(String string) {
		return (null == string ? null : TradeType.getByValue(string));
	}
}
