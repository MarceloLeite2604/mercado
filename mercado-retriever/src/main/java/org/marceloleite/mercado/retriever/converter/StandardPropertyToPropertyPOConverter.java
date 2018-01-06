package org.marceloleite.mercado.retriever.converter;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databasemodel.PropertyPO;
import org.marceloleite.mercado.properties.StandardProperty;

public class StandardPropertyToPropertyPOConverter implements Converter<StandardProperty, PropertyPO> {

	@Override
	public PropertyPO convertTo(StandardProperty standardProperty) {
		PropertyPO propertyPO = new PropertyPO();
		propertyPO.setName(standardProperty.getName());
		propertyPO.setValue(standardProperty.getValue());
		return propertyPO;
	}

	@Override
	public StandardProperty convertFrom(PropertyPO propertyPO) {
		throw new UnsupportedOperationException();
	}

}
