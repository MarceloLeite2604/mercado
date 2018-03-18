package org.marceloleite.mercado.xml.converters;

import org.marceloleite.mercado.data.OperationData;
import org.marceloleite.mercado.xml.structures.XmlOperation;

public class XmlOperationConverter implements XmlConverter<XmlOperation, OperationData> {

	@Override
	public XmlOperation convertToXml(OperationData operationData) {
		XmlOperation xmlOperation = new XmlOperation();
		xmlOperation.setExecuted(operationData.getExecuted());
		xmlOperation.setFeeRate(operationData.getFeeRate());
		xmlOperation.setId(operationData.getId());
		xmlOperation.setPrice(operationData.getPrice());
		xmlOperation.setQuantity(operationData.getQuantity());
		return null;
	}

	@Override
	public OperationData convertToObject(XmlOperation xmlOperation) {
		// TODO Auto-generated method stub
		return null;
	}

}
