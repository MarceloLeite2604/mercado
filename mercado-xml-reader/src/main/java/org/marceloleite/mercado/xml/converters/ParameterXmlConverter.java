package org.marceloleite.mercado.xml.converters;

import org.marceloleite.mercado.base.model.data.ParameterData;
import org.marceloleite.mercado.xml.structures.XmlParameter;

public class ParameterXmlConverter implements XmlConverter<XmlParameter, ParameterData> {

	@Override
	public XmlParameter convertToXml(ParameterData parameterData) {
		XmlParameter xmlParameter = new XmlParameter();
		xmlParameter.setName(parameterData.getName());
		xmlParameter.setValue(parameterData.getValue());
		return xmlParameter;
	}

	@Override
	public ParameterData convertToObject(XmlParameter xmlParameter) {
		ParameterData parameterData = new ParameterData();
		parameterData.setName(xmlParameter.getName());
		parameterData.setValue(xmlParameter.getValue());
		return parameterData;
	}

}
