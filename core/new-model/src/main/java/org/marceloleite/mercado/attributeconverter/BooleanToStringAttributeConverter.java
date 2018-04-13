package org.marceloleite.mercado.attributeconverter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class BooleanToStringAttributeConverter implements AttributeConverter<Boolean, String> {

	@Override
	public String convertToDatabaseColumn(Boolean attribute) {
		return attribute.toString();
	}

	@Override
	public Boolean convertToEntityAttribute(String dbData) {
		return Boolean.parseBoolean(dbData);
	}

}
