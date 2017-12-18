package org.marceloleite.mercado.modeler.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.marceloleite.mercado.commons.Currency;

@Converter(autoApply = true)
public class CurrencyAttributeConverter implements AttributeConverter<Currency, String> {

	@Override
	public String convertToDatabaseColumn(Currency currency) {
		return (null == currency ? null : currency.getAcronym());
	}

	@Override
	public Currency convertToEntityAttribute(String string) {
		return (null == string ? null : Currency.getByAcronym(string));
	}
}
