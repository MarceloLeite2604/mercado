package org.marceloleite.mercado.converter.json.api.negotiation;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.CurrencyAmountData;
import org.marceloleite.mercado.data.OrderData;
import org.marceloleite.mercado.data.SellOrderData;
import org.marceloleite.mercado.negotiationapi.model.placesellorder.PlaceSellOrderResult;

public class PlaceSellOrderResultToSellOrderDataConverter implements Converter <PlaceSellOrderResult, SellOrderData>{

	@Override
	public SellOrderData convertTo(PlaceSellOrderResult placeSellOrderResponse) {
		SellOrderData sellOrderData = new SellOrderData();
		OrderData orderData = placeSellOrderResponse.getOrder();
		
		Currency currencyToReceive = orderData.getFirstCurrency();
		double amountToReceive = orderData.getLimitPrice();
		CurrencyAmountData currencyAmountDataToReceive = new CurrencyAmountData(currencyToReceive, amountToReceive);
		
		Currency currencyToSell = orderData.getSecondCurrency();
		double amountToSell = orderData.getExecutedQuantity();
		CurrencyAmountData currencyAmountDataToSell = new CurrencyAmountData(currencyToSell, amountToSell);
		
		sellOrderData.setCurrencyAmountToReceive(currencyAmountDataToReceive);
		sellOrderData.setCurrencyAmountToSell(currencyAmountDataToSell);
		return sellOrderData;
	}

	@Override
	public PlaceSellOrderResult convertFrom(SellOrderData sellOrderData) {
		throw new UnsupportedOperationException();
	}

}
