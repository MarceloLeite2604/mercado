package org.marceloleite.mercado.xml.converters;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.data.ClassData;
import org.marceloleite.mercado.xml.structures.XmlClass;

public class ListClassXmlConverter implements XmlConverter<List<XmlClass>, List<ClassData>> {

	@Override
	public List<XmlClass> convertToXml(List<ClassData> classDatas) {
		ClassXmlConverter classXmlConverter = new ClassXmlConverter();
		List<XmlClass> xmlClasses = new ArrayList<>();
		for (ClassData classData : classDatas) {
			xmlClasses.add(classXmlConverter.convertToXml(classData));
		}
		return xmlClasses;
	}

	@Override
	public List<ClassData> convertToObject(List<XmlClass> xmlClasses) {
		ClassXmlConverter classXmlConverter = new ClassXmlConverter();
		List<ClassData> classDatas = new ArrayList<>();
		for (XmlClass xmlClass : xmlClasses) {
			classDatas.add(classXmlConverter.convertToObject(xmlClass));
		}
		return classDatas;
	}

}
