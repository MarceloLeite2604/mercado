package org.marceloleite.mercado.converter.xml;

import org.marceloleite.mercado.simulator.structure.BuyOrder;
import org.marceloleite.mercado.simulator.structure.CurrencyAmount;
import org.marceloleite.mercado.xml.structures.XmlBuyOrder;

public class BuyOrderXmlConverter implements XmlConverter<XmlBuyOrder, BuyOrder> {

	@Override
	public XmlBuyOrder convertToXml(BuyOrder buyOrder) {
		XmlBuyOrder xmlBuyOrder = new XmlBuyOrder();
		xmlBuyOrder.setTime(buyOrder.getTime());
		CurrencyAmount currencyAmountToBuy = buyOrder.getCurrencyAmountToBuy();
		CurrencyAmount currencyAmountToPay = buyOrder.getCurrencyAmountToPay();
		xmlBuyOrder.setCurrencyToBuy(currencyAmountToBuy.getCurrency());
		xmlBuyOrder.setAmountToBuy(currencyAmountToBuy.getAmount());
		xmlBuyOrder.setCurrencyToPay(currencyAmountToPay.getCurrency());
		xmlBuyOrder.setAmountToPay(currencyAmountToPay.getAmount());

		return xmlBuyOrder;
	}

	@Override
	public BuyOrder convertToObject(XmlBuyOrder xmlBuyOrder) {
		BuyOrder buyOrder = new BuyOrder();
		buyOrder.setTime(xmlBuyOrder.getTime());
		CurrencyAmount currencyAmountToBuy = buyOrder.getCurrencyAmountToBuy();
		currencyAmountToBuy.setAmount(xmlBuyOrder.getAmountToBuy());
		currencyAmountToBuy.setCurrency(xmlBuyOrder.getCurrencyToBuy());
		CurrencyAmount currencyAmountToPay = buyOrder.getCurrencyAmountToPay();
		currencyAmountToPay.setAmount(xmlBuyOrder.getAmountToPay());
		currencyAmountToPay.setCurrency(xmlBuyOrder.getCurrencyToPay());
		return buyOrder;
	}

}
