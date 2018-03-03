package org.marceloleite.mercado.retriever.converter;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.commons.properties.StandardProperty;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PropertyPO;

public class PropertyPOToStandardPropertyConverter implements Converter<PropertyPO, StandardProperty> {

	@Override
	public StandardProperty convertTo(PropertyPO propertyPO) {
		StandardProperty standardProperty = new StandardProperty();
		standardProperty.setName(propertyPO.getName());
		standardProperty.setValue(propertyPO.getValue());
		return standardProperty;
	}

	@Override
	public PropertyPO convertFrom(StandardProperty standardProperty) {
		throw new UnsupportedOperationException();
	}

}
