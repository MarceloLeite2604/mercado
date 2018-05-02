package org.marceloleite.mercado.api.negotiation.converters.placebuyorder;

import org.marceloleite.mercado.api.negotiation.methods.placebuyorder.PlaceBuyOrderResponse;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.BuyOrderData;
import org.marceloleite.mercado.data.CurrencyAmountData;
import org.marceloleite.mercado.data.OrderData;

public class OldPlaceBuyOrderResponseToBuyOrderDataConverter implements Converter <PlaceBuyOrderResponse, BuyOrderData>{

	@Override
	public BuyOrderData convertTo(PlaceBuyOrderResponse placeBuyOrderResponse) {
		BuyOrderData buyOrderData = new BuyOrderData();
		OrderData orderData = placeBuyOrderResponse.getOrderData();
		
		Currency currencyToPay = orderData.getFirstCurrency();
		MercadoBigDecimal amountToPay = orderData.getLimitPrice();
		CurrencyAmountData currencyAmountDataToPay = new CurrencyAmountData(currencyToPay, amountToPay);
		
		Currency currencyToBuy = orderData.getSecondCurrency();
		MercadoBigDecimal amountToBuy = orderData.getExecutedQuantity();
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
