package org.marceloleite.mercado.converter.xml;

import org.marceloleite.mercado.simulator.structure.BuyOrder;
import org.marceloleite.mercado.xml.structures.XmlBuyOrder;

public class BuyOrderXmlConverter implements XmlConverter<XmlBuyOrder, BuyOrder> {

	@Override
	public XmlBuyOrder convertToXml(BuyOrder buyOrder) {
		XmlBuyOrder xmlBuyOrder = new XmlBuyOrder();
		xmlBuyOrder.setCurrencyToBuy(buyOrder.getCurrencyToBuy());
		xmlBuyOrder.setAmountToBuy(buyOrder.getAmountToBuy());
		xmlBuyOrder.setCurrencyToPay(buyOrder.getCurrencyToPay());
		xmlBuyOrder.setAmountToPay(buyOrder.getAmountToPay());

		return xmlBuyOrder;
	}

	@Override
	public BuyOrder convertToObject(XmlBuyOrder xmlBuyOrder) {
		BuyOrder buyOrder = new BuyOrder();
		buyOrder.setCurrencyToBuy(xmlBuyOrder.getCurrencyToBuy());
		buyOrder.setAmountToBuy(xmlBuyOrder.getAmountToBuy());
		buyOrder.setCurrencyToPay(xmlBuyOrder.getCurrencyToPay());
		buyOrder.setAmountToPay(xmlBuyOrder.getAmountToPay());
		return buyOrder;
	}

}
