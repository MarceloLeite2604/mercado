package org.marceloleite.mercado.xml.converters;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.data.OperationData;
import org.marceloleite.mercado.xml.structures.XmlOperation;

public class ListOperationXmlConverter implements XmlConverter<List<XmlOperation>, List<OperationData>> {

	@Override
	public List<XmlOperation> convertToXml(List<OperationData> operationDatas) {
		List<XmlOperation> xmlOperations = new ArrayList<>();
		if (operationDatas != null && !operationDatas.isEmpty()) {
			XmlOperationConverter xmlOperationConverter = new XmlOperationConverter();
			for (OperationData operationData : operationDatas) {
				xmlOperations.add(xmlOperationConverter.convertToXml(operationData));
			}
		}
		return xmlOperations;
	}

	@Override
	public List<OperationData> convertToObject(List<XmlOperation> xmlOperations) {
		List<OperationData> operationDatas = new ArrayList<>();
		if (xmlOperations != null && !xmlOperations.isEmpty()) {
			XmlOperationConverter xmlOperationConverter = new XmlOperationConverter();
			for (XmlOperation xmlOperation : xmlOperations) {
				operationDatas.add(xmlOperationConverter.convertToObject(xmlOperation));
			}
		}
		return operationDatas;
	}

}
