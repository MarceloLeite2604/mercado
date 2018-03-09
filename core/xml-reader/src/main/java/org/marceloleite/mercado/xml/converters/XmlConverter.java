package org.marceloleite.mercado.xml.converters;

public interface XmlConverter<XML, OBJECT> {

	XML convertToXml(OBJECT object);
	
	OBJECT convertToObject(XML xml);
}
