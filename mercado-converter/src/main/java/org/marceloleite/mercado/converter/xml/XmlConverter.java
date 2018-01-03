package org.marceloleite.mercado.converter.xml;

public interface XmlConverter<XML, OBJECT> {

	XML convertToXml(OBJECT object);
	
	OBJECT convertToObject(XML xml);
}
