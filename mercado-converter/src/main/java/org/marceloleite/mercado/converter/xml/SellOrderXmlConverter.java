package org.marceloleite.mercado.converter.xml;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.simulator.data.CurrencyAmountData;
import org.marceloleite.mercado.simulator.data.SellOrderData;
import org.marceloleite.mercado.xml.structures.XmlSellOrder;

public class SellOrderXmlConverter implements XmlConverter<XmlSellOrder, SellOrderData> {

	@Override
	public XmlSellOrder convertToXml(SellOrderData sellOrder) {
		XmlSellOrder xmlSellOrder = new XmlSellOrder();
		xmlSellOrder.setTime(sellOrder.getTime());
		CurrencyAmountData currencyAmountToSell = sellOrder.getCurrencyAmountToSell();
		CurrencyAmountData currencyAmountToReceive = sellOrder.getCurrencyAmountToReceive();
		xmlSellOrder.setCurrencyToSell(currencyAmountToSell.getCurrency());
		xmlSellOrder.setAmountToSell(currencyAmountToSell.getAmount());
		xmlSellOrder.setCurrencyToReceive(currencyAmountToReceive.getCurrency());
		xmlSellOrder.setAmountToReceive(currencyAmountToReceive.getAmount());

		return xmlSellOrder;
	}

	@Override
	public SellOrderData convertToObject(XmlSellOrder xmlSellOrder) {
		SellOrderData sellOrderData = new SellOrderData();
		sellOrderData.setTime(xmlSellOrder.getTime());

		Double amountToSell = xmlSellOrder.getAmountToSell();
		Currency currencyToSell = xmlSellOrder.getCurrencyToSell();
		CurrencyAmountData currencyAmountToSell = new CurrencyAmountData(currencyToSell, amountToSell);
		Double amountToReceive = xmlSellOrder.getAmountToReceive();
		Currency currencyToReceive = xmlSellOrder.getCurrencyToReceive();
		CurrencyAmountData currencyAmountToReceive = new CurrencyAmountData(currencyToReceive, amountToReceive);
		sellOrderData.setCurrencyAmountToSell(currencyAmountToSell);
		sellOrderData.setCurrencyAmountToReceive(currencyAmountToReceive);
		return sellOrderData;
	}

}
