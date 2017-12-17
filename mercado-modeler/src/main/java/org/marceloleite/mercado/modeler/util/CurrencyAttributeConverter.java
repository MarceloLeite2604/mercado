package org.marceloleite.mercado.modeler.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.marceloleite.mercado.consumer.model.Currency;

@Converter(autoApply = true)
public class CurrencyAttributeConverter implements AttributeConverter<Currency, String> {

	@Override
	public String convertToDatabaseColumn(Currency currency) {
		return (null == currency ? null : currency.getAcronimo());
	}

	@Override
	public Currency convertToEntityAttribute(String string) {
		return (null == string ? null : Currency.getByAcronimo(string));
	}
}
