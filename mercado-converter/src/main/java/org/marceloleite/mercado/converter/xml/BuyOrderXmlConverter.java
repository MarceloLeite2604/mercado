package org.marceloleite.mercado.converter.xml;

import org.marceloleite.mercado.simulator.structure.BuyOrderData;
import org.marceloleite.mercado.simulator.structure.CurrencyAmount;
import org.marceloleite.mercado.xml.structures.XmlBuyOrder;

public class BuyOrderXmlConverter implements XmlConverter<XmlBuyOrder, BuyOrderData> {

	@Override
	public XmlBuyOrder convertToXml(BuyOrderData buyOrder) {
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
	public BuyOrderData convertToObject(XmlBuyOrder xmlBuyOrder) {
		BuyOrderData buyOrderData = new BuyOrderData();
		buyOrderData.setTime(xmlBuyOrder.getTime());
		CurrencyAmount currencyAmountToBuy = buyOrderData.getCurrencyAmountToBuy();
		currencyAmountToBuy.setAmount(xmlBuyOrder.getAmountToBuy());
		currencyAmountToBuy.setCurrency(xmlBuyOrder.getCurrencyToBuy());
		CurrencyAmount currencyAmountToPay = buyOrderData.getCurrencyAmountToPay();
		currencyAmountToPay.setAmount(xmlBuyOrder.getAmountToPay());
		currencyAmountToPay.setCurrency(xmlBuyOrder.getCurrencyToPay());
		return buyOrderData;
	}

}
