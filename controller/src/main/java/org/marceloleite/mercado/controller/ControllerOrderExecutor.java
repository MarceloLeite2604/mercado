package org.marceloleite.mercado.controller;

import org.marceloleite.mercado.api.negotiation.methods.placebuyorder.PlaceBuyOrderMethod;
import org.marceloleite.mercado.api.negotiation.methods.placebuyorder.PlaceBuyOrderMethodResponse;
import org.marceloleite.mercado.api.negotiation.methods.placesellorder.PlaceSellOrderMethod;
import org.marceloleite.mercado.api.negotiation.methods.placesellorder.PlaceSellOrderMethodResult;
import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.OrderExecutor;
import org.marceloleite.mercado.base.model.order.BuyOrderBuilder.BuyOrder;
import org.marceloleite.mercado.base.model.order.SellOrderBuilder.SellOrder;
import org.marceloleite.mercado.converter.json.api.negotiation.PlaceBuyOrderResponseToBuyOrderDataConverter;
import org.marceloleite.mercado.converter.json.api.negotiation.PlaceSellOrderResultToSellOrderDataConverter;
import org.marceloleite.mercado.data.BuyOrderData;
import org.marceloleite.mercado.data.SellOrderData;
import org.marceloleite.mercado.negotiationapi.model.CurrencyPair;
import org.marceloleite.mercado.negotiationapi.model.placebuyorder.PlaceBuyOrderResponse;
import org.marceloleite.mercado.negotiationapi.model.placesellorder.PlaceSellOrderResult;

public class ControllerOrderExecutor implements OrderExecutor {

	@Override
	public BuyOrder executeBuyOrder(BuyOrder buyOrder, House house, Account account) {
		PlaceBuyOrderMethod placeBuyOrderMethod = new PlaceBuyOrderMethod(account.getTapiInformation());
		CurrencyAmount currencyAmountToBuy = buyOrder.getCurrencyAmountToBuy();
		CurrencyAmount currencyAmountToPay = buyOrder.getCurrencyAmountToPay();
		CurrencyPair currencyPair = CurrencyPair.retrieveByPair(currencyAmountToPay.getCurrency(), currencyAmountToBuy.getCurrency());
		double quantity = currencyAmountToBuy.getAmount();
		double limitPrice = currencyAmountToPay.getAmount();
		PlaceBuyOrderMethodResponse placeBuyOrderMethodResponse = placeBuyOrderMethod.execute(currencyPair, quantity, limitPrice);
		if ( placeBuyOrderMethodResponse.getStatusCode() == 100 ) {
			PlaceBuyOrderResponse placeBuyOrderResponse = placeBuyOrderMethodResponse.getResponse();
			BuyOrderData buyOrderData = new PlaceBuyOrderResponseToBuyOrderDataConverter().convertTo(placeBuyOrderResponse);
			return new BuyOrder(buyOrderData);
		}
		return null;
	}

	@Override
	public SellOrder executeSellOrder(SellOrder sellOrder, House house, Account account) {
		PlaceSellOrderMethod placeSellOrderMethod = new PlaceSellOrderMethod(account.getTapiInformation());
		CurrencyAmount currencyAmountToSell = sellOrder.getCurrencyAmountToSell();
		CurrencyAmount currencyAmountToReceive = sellOrder.getCurrencyAmountToReceive();
		CurrencyPair currencyPair = CurrencyPair.retrieveByPair(currencyAmountToReceive.getCurrency(), currencyAmountToSell.getCurrency());
		double quantity = currencyAmountToSell.getAmount();
		double limitPrice = currencyAmountToReceive.getAmount();
		PlaceSellOrderMethodResult placeSellOrderMethodResponse = placeSellOrderMethod.execute(currencyPair, quantity, limitPrice);
		if ( placeSellOrderMethodResponse.getStatusCode() == 100 ) {
			PlaceSellOrderResult placeSellOrderResponse = placeSellOrderMethodResponse.getResponse();
			SellOrderData sellOrderData = new PlaceSellOrderResultToSellOrderDataConverter().convertTo(placeSellOrderResponse);
			return new SellOrder(sellOrderData);
		}
		return null;
	}

}
