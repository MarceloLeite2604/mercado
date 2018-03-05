package org.marceloleite.mercado.converter.json.api.negotiation;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.BuyOrderData;
import org.marceloleite.mercado.data.CurrencyAmountData;
import org.marceloleite.mercado.negotiationapi.model.CurrencyPair;
import org.marceloleite.mercado.negotiationapi.model.Order;
import org.marceloleite.mercado.negotiationapi.model.placebuyorder.PlaceBuyOrderResponse;

public class PlaceBuyOrderResponseToBuyOrderDataConverter implements Converter <PlaceBuyOrderResponse, BuyOrderData>{

	@Override
	public BuyOrderData convertTo(PlaceBuyOrderResponse placeBuyOrderResponse) {
		BuyOrderData buyOrderData = new BuyOrderData();
		Order order = placeBuyOrderResponse.getOrder();
		CurrencyPair currencyPair = order.getCurrencyPair();
		
		Currency currencyToPay = currencyPair.getFirstCurrency();
		double amountToPay = order.getLimitPrice();
		CurrencyAmountData currencyAmountDataToPay = new CurrencyAmountData(currencyToPay, amountToPay);
		
		Currency currencyToBuy = currencyPair.getSecondCurrency();
		double amountToBuy = order.getExecutedQuantity();
		CurrencyAmountData currencyAmountDataToBuy = new CurrencyAmountData(currencyToBuy, amountToBuy);
		
		buyOrderData.setCurrencyAmountToPay(currencyAmountDataToPay);
		buyOrderData.setCurrencyAmountToBuy(currencyAmountDataToBuy);
		return buyOrderData;
	}

	@Override
	public PlaceBuyOrderResponse convertFrom(BuyOrderData buyOrderData) {
		throw new UnsupportedOperationException();
	}

}
