package org.marceloleite.mercado.retriever.converter;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.commons.properties.StandardProperty;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PropertyPO;

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
