package org.marceloleite.mercado.retriever.converter;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.commons.properties.StandardProperty;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PropertyPO;

public class PropertyPOToStandardPropertyConverter implements Converter<PropertyPO, StandardProperty> {

	@Override
	public StandardProperty convertTo(PropertyPO propertyPO) {
		String name = propertyPO.getName();
		String value = propertyPO.getValue();
		StandardProperty standardProperty = new StandardProperty(name, value, false);
		return standardProperty;
	}

	@Override
	public PropertyPO convertFrom(StandardProperty standardProperty) {
		throw new UnsupportedOperationException();
	}

}
