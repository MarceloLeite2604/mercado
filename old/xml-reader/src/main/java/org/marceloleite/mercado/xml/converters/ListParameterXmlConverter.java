package org.marceloleite.mercado.xml.converters;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.data.ParameterData;
import org.marceloleite.mercado.xml.structures.XmlParameter;

public class ListParameterXmlConverter implements XmlConverter<List<XmlParameter>, List<ParameterData>> {

	@Override
	public List<XmlParameter> convertToXml(List<ParameterData> parameterDatas) {
		List<XmlParameter> xmlParameters = new ArrayList<>();
		ParameterXmlConverter parameterXmlConverter = new ParameterXmlConverter();
		for (ParameterData parameterData : parameterDatas) {
			xmlParameters.add(parameterXmlConverter.convertToXml(parameterData));
		}
		return xmlParameters;
	}

	@Override
	public List<ParameterData> convertToObject(List<XmlParameter> xmlParameters) {
		List<ParameterData> parameterDatas = new ArrayList<>();
		ParameterXmlConverter parameterXmlConverter = new ParameterXmlConverter();
		if (xmlParameters != null && !xmlParameters.isEmpty()) {
			for (XmlParameter xmlParameter : xmlParameters) {
				parameterDatas.add(parameterXmlConverter.convertToObject(xmlParameter));
			}
		}
		return parameterDatas;
	}

}
