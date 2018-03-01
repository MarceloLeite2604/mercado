package org.marceloleite.mercado.xml.converters;

import java.util.List;

import org.marceloleite.mercado.base.model.data.ClassData;
import org.marceloleite.mercado.base.model.data.ParameterData;
import org.marceloleite.mercado.xml.structures.XmlClass;
import org.marceloleite.mercado.xml.structures.XmlParameter;

public class ClassXmlConverter implements XmlConverter<XmlClass, ClassData> {

	@Override
	public XmlClass convertToXml(ClassData classData) {
		XmlClass xmlClass = new XmlClass();
		xmlClass.setName(classData.getName());
		ListParameterXmlConverter listParameterXmlConverter = new ListParameterXmlConverter();
		List<XmlParameter> xmlParameters = listParameterXmlConverter.convertToXml(classData.getParameterDatas());
		xmlClass.setXmlParameters(xmlParameters);
		return xmlClass;
	}

	@Override
	public ClassData convertToObject(XmlClass xmlClass) {
		ListParameterXmlConverter listParameterXmlConverter = new ListParameterXmlConverter();
		ClassData classData = new ClassData();
		classData.setName(xmlClass.getName());
		List<ParameterData> parameterDatas = listParameterXmlConverter.convertToObject(xmlClass.getXmlParameters());
		parameterDatas.forEach(parameterData -> parameterData.setClassData(classData));
		classData.setParameterDatas(parameterDatas);
		return classData;
	}

}
