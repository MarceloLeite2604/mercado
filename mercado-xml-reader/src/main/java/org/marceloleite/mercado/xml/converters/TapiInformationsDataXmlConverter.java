package org.marceloleite.mercado.xml.converters;

import org.marceloleite.mercado.data.TapiInformationData;
import org.marceloleite.mercado.xml.structures.XmlTapiInformation;

public class TapiInformationsDataXmlConverter implements XmlConverter<XmlTapiInformation, TapiInformationData>{

	@Override
	public XmlTapiInformation convertToXml(TapiInformationData tapiInformationData) {
		XmlTapiInformation xmlTapiInformation = new XmlTapiInformation();
		xmlTapiInformation.setId(tapiInformationData.getId());
		xmlTapiInformation.setSecret(tapiInformationData.getSecret());
		return xmlTapiInformation;
	}

	@Override
	public TapiInformationData convertToObject(XmlTapiInformation xmlTapiInformation) {
		TapiInformationData tapiInformationData = new TapiInformationData();
		tapiInformationData.setId(xmlTapiInformation.getId());
		tapiInformationData.setSecret(xmlTapiInformation.getSecret());
		return tapiInformationData;
	}

}
