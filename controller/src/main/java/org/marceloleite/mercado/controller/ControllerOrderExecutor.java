package org.marceloleite.mercado.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.api.negotiation.methods.placebuyorder.PlaceBuyOrderMethod;
import org.marceloleite.mercado.api.negotiation.methods.placebuyorder.PlaceBuyOrderMethodResponse;
import org.marceloleite.mercado.api.negotiation.methods.placebuyorder.PlaceBuyOrderResponse;
import org.marceloleite.mercado.api.negotiation.methods.placesellorder.PlaceSellOrderMethod;
import org.marceloleite.mercado.api.negotiation.methods.placesellorder.PlaceSellOrderMethodResponse;
import org.marceloleite.mercado.api.negotiation.methods.placesellorder.PlaceSellOrderResponse;
import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.Order;
import org.marceloleite.mercado.base.model.OrderExecutor;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderStatus;
import org.marceloleite.mercado.data.OrderData;
import org.marceloleite.mercado.negotiationapi.model.CurrencyPair;

public class ControllerOrderExecutor implements OrderExecutor {
	
	private static final Logger LOGGER = LogManager.getLogger(ControllerOrderExecutor.class);

	@Override
	public Order placeOrder(Order order, House house, Account account) {
		Order resultOrder = null;
		switch (order.getType()) {
		case BUY:
			resultOrder = placeBuyOrder(order, account);
		case SELL:
			resultOrder = placeSellOrder(order, account);
		}
		return resultOrder;
	}

	private Order placeSellOrder(Order order, Account account) {
		Currency firstCurrency = order.getFirstCurrency();
		Currency secondCurrency = order.getSecondCurrency();
		CurrencyPair currencyPair = CurrencyPair.retrieveByPair(firstCurrency, secondCurrency);
		Double quantity = order.getQuantity().doubleValue();
		Double limitPrice = order.getLimitPrice().doubleValue();
		
		PlaceSellOrderMethod placeSellOrderMethod = new PlaceSellOrderMethod(account.getTapiInformation());
		PlaceSellOrderMethodResponse placeSellOrderMethodResponse = placeSellOrderMethod.execute(currencyPair, quantity, limitPrice);
		if ( placeSellOrderMethodResponse.getStatusCode() == 100 ) {
			PlaceSellOrderResponse placeSellOrderResponse = placeSellOrderMethodResponse.getResponse();
			OrderData orderData = placeSellOrderResponse.getOrderData();
			return new Order(orderData);
		} else {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("Place sell order method returned the following error: ");
			stringBuilder.append(placeSellOrderMethodResponse.getStatusCode() + " - ");
			stringBuilder.append(placeSellOrderMethodResponse.getErrorMessage());
			LOGGER.warn(stringBuilder.toString());
			order.setStatus(OrderStatus.CANCELLED);
			return order;
		}
	}

	private Order placeBuyOrder(Order order, Account account) {
		PlaceBuyOrderMethod placeBuyOrderMethod = new PlaceBuyOrderMethod(account.getTapiInformation());
		CurrencyPair currencyPair = CurrencyPair.retrieveByPair(order.getFirstCurrency(), order.getSecondCurrency());
		double quantity = order.getQuantity().doubleValue();
		double limitPrice = order.getLimitPrice().doubleValue();
		PlaceBuyOrderMethodResponse placeBuyOrderMethodResponse = placeBuyOrderMethod.execute(currencyPair, quantity, limitPrice);
		if ( placeBuyOrderMethodResponse.getStatusCode() == 100 ) {
			PlaceBuyOrderResponse placeBuyOrderResponse = placeBuyOrderMethodResponse.getResponse();
			OrderData orderData = placeBuyOrderResponse.getOrderData();
			return new Order(orderData);
		} else {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("Place buy order method returned the following error: ");
			stringBuilder.append(placeBuyOrderMethodResponse.getStatusCode() + " - ");
			stringBuilder.append(placeBuyOrderMethodResponse.getErrorMessage());
			LOGGER.warn(stringBuilder.toString());
			order.setStatus(OrderStatus.CANCELLED);
			return order;
		}
	}

}
