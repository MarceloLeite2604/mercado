package org.marceloleite.mercado.converter.xml;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.simulator.data.BuyOrderData;
import org.marceloleite.mercado.simulator.data.CurrencyAmountData;
import org.marceloleite.mercado.xml.structures.XmlBuyOrder;

public class BuyOrderXmlConverter implements XmlConverter<XmlBuyOrder, BuyOrderData> {

	@Override
	public XmlBuyOrder convertToXml(BuyOrderData buyOrder) {
		XmlBuyOrder xmlBuyOrder = new XmlBuyOrder();
		xmlBuyOrder.setTime(buyOrder.getTime());
		CurrencyAmountData currencyAmountToBuy = buyOrder.getCurrencyAmountToBuy();
		CurrencyAmountData currencyAmountToPay = buyOrder.getCurrencyAmountToPay();
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

		Double amountToBuy = xmlBuyOrder.getAmountToBuy();
		Currency currencyToBuy = xmlBuyOrder.getCurrencyToBuy();
		CurrencyAmountData currencyAmountToBuy = new CurrencyAmountData(currencyToBuy, amountToBuy);
		Double amountToPay = xmlBuyOrder.getAmountToPay();
		Currency currencyToPay = xmlBuyOrder.getCurrencyToPay();
		CurrencyAmountData currencyAmountToPay = new CurrencyAmountData(currencyToPay, amountToPay);
		buyOrderData.setCurrencyAmountToBuy(currencyAmountToBuy);
		buyOrderData.setCurrencyAmountToPay(currencyAmountToPay);
		return buyOrderData;
	}

}
