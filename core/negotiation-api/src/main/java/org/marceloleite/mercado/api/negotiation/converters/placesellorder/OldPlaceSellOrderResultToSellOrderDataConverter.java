package org.marceloleite.mercado.api.negotiation.converters.placesellorder;

import org.marceloleite.mercado.api.negotiation.methods.placesellorder.PlaceSellOrderResponse;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.CurrencyAmountData;
import org.marceloleite.mercado.data.OrderData;
import org.marceloleite.mercado.data.SellOrderData;

public class OldPlaceSellOrderResultToSellOrderDataConverter implements Converter <PlaceSellOrderResponse, SellOrderData>{

	@Override
	public SellOrderData convertTo(PlaceSellOrderResponse placeSellOrderResponse) {
		SellOrderData sellOrderData = new SellOrderData();
		OrderData orderData = placeSellOrderResponse.getOrderData();
		
		Currency currencyToReceive = orderData.getFirstCurrency();
		MercadoBigDecimal amountToReceive = orderData.getLimitPrice();
		CurrencyAmountData currencyAmountDataToReceive = new CurrencyAmountData(currencyToReceive, amountToReceive);
		
		Currency currencyToSell = orderData.getSecondCurrency();
		MercadoBigDecimal amountToSell = orderData.getExecutedQuantity();
		CurrencyAmountData currencyAmountDataToSell = new CurrencyAmountData(currencyToSell, amountToSell);
		
		sellOrderData.setCurrencyAmountToReceive(currencyAmountDataToReceive);
		sellOrderData.setCurrencyAmountToSell(currencyAmountDataToSell);
		return sellOrderData;
	}

	@Override
	public PlaceSellOrderResponse convertFrom(SellOrderData sellOrderData) {
		throw new UnsupportedOperationException();
	}

}
