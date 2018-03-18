package org.marceloleite.mercado.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.api.negotiation.converters.placebuyorder.PlaceBuyOrderResponseToBuyOrderDataConverter;
import org.marceloleite.mercado.api.negotiation.converters.placesellorder.PlaceSellOrderResultToSellOrderDataConverter;
import org.marceloleite.mercado.api.negotiation.methods.placebuyorder.PlaceBuyOrderMethod;
import org.marceloleite.mercado.api.negotiation.methods.placebuyorder.PlaceBuyOrderMethodResponse;
import org.marceloleite.mercado.api.negotiation.methods.placebuyorder.PlaceBuyOrderResponse;
import org.marceloleite.mercado.api.negotiation.methods.placesellorder.PlaceSellOrderMethod;
import org.marceloleite.mercado.api.negotiation.methods.placesellorder.PlaceSellOrderMethodResponse;
import org.marceloleite.mercado.api.negotiation.methods.placesellorder.PlaceSellOrderResponse;
import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.OrderExecutor;
import org.marceloleite.mercado.base.model.order.BuyOrderBuilder.BuyOrder;
import org.marceloleite.mercado.base.model.order.OrderStatus;
import org.marceloleite.mercado.base.model.order.SellOrderBuilder.SellOrder;
import org.marceloleite.mercado.data.BuyOrderData;
import org.marceloleite.mercado.data.SellOrderData;
import org.marceloleite.mercado.negotiationapi.model.CurrencyPair;

public class ControllerOrderExecutor implements OrderExecutor {
	
	private static final Logger LOGGER = LogManager.getLogger(ControllerOrderExecutor.class);

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
		} else {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("Place buy order method returned the following error: ");
			stringBuilder.append(placeBuyOrderMethodResponse.getStatusCode() + " - ");
			stringBuilder.append(placeBuyOrderMethodResponse.getErrorMessage());
			LOGGER.warn(stringBuilder.toString());
			buyOrder.setOrderStatus(OrderStatus.CANCELLED);
			return buyOrder;
		}
	}

	@Override
	public SellOrder executeSellOrder(SellOrder sellOrder, House house, Account account) {
		PlaceSellOrderMethod placeSellOrderMethod = new PlaceSellOrderMethod(account.getTapiInformation());
		CurrencyAmount currencyAmountToSell = sellOrder.getCurrencyAmountToSell();
		CurrencyAmount currencyAmountToReceive = sellOrder.getCurrencyAmountToReceive();
		CurrencyPair currencyPair = CurrencyPair.retrieveByPair(currencyAmountToReceive.getCurrency(), currencyAmountToSell.getCurrency());
		double quantity = currencyAmountToSell.getAmount();
		double limitPrice = currencyAmountToReceive.getAmount();
		PlaceSellOrderMethodResponse placeSellOrderMethodResponse = placeSellOrderMethod.execute(currencyPair, quantity, limitPrice);
		if ( placeSellOrderMethodResponse.getStatusCode() == 100 ) {
			PlaceSellOrderResponse placeSellOrderResponse = placeSellOrderMethodResponse.getResponse();
			SellOrderData sellOrderData = new PlaceSellOrderResultToSellOrderDataConverter().convertTo(placeSellOrderResponse);
			return new SellOrder(sellOrderData);
		} else {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("Place sell order method returned the following error: ");
			stringBuilder.append(placeSellOrderMethodResponse.getStatusCode() + " - ");
			stringBuilder.append(placeSellOrderMethodResponse.getErrorMessage());
			LOGGER.warn(stringBuilder.toString());
			sellOrder.setOrderStatus(OrderStatus.CANCELLED);
			return sellOrder;
		}
	}

}
