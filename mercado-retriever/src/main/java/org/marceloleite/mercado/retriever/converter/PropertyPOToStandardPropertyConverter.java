package org.marceloleite.mercado.retriever.converter;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databasemodel.PropertyPO;
import org.marceloleite.mercado.properties.StandardProperty;

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
