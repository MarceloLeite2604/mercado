package org.marceloleite.mercado.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.CurrencyPair;
import org.marceloleite.mercado.House;
import org.marceloleite.mercado.OrderExecutor;
import org.marceloleite.mercado.api.negotiation.method.PlaceBuyOrder;
import org.marceloleite.mercado.api.negotiation.method.PlaceSellOrder;
import org.marceloleite.mercado.api.negotiation.method.TapiResponse;
import org.marceloleite.mercado.commons.OrderStatus;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Order;

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
		Double quantity = order.getQuantity().doubleValue();
		Double limitPrice = order.getLimitPrice().doubleValue();
		CurrencyPair currencyPair = order.getCurrencyPair();
		
		PlaceSellOrder placeSellOrder = new PlaceSellOrder(account.getTapiInformation());
		TapiResponse<Order> tapiResponse = placeSellOrder.execute(currencyPair, quantity, limitPrice);
		if ( tapiResponse.getStatusCode() == 100 ) {
			return tapiResponse.getResponseData();
		} else {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("Place sell order method returned the following error: ");
			stringBuilder.append(tapiResponse.getStatusCode() + " - ");
			stringBuilder.append(tapiResponse.getErrorMessage());
			LOGGER.warn(stringBuilder.toString());
			order.setStatus(OrderStatus.CANCELLED);
			return order;
		}
	}

	private Order placeBuyOrder(Order order, Account account) {
		PlaceBuyOrder placeBuyOrder = new PlaceBuyOrder(account.getTapiInformation());
		CurrencyPair currencyPair = order.getCurrencyPair();
		double quantity = order.getQuantity().doubleValue();
		double limitPrice = order.getLimitPrice().doubleValue();
		TapiResponse<Order> tapiResponse = placeBuyOrder.execute(currencyPair, quantity, limitPrice);
		if ( tapiResponse.getStatusCode() == 100 ) {
			return tapiResponse.getResponseData();
		} else {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("Place buy order method returned the following error: ");
			stringBuilder.append(tapiResponse.getStatusCode() + " - ");
			stringBuilder.append(tapiResponse.getErrorMessage());
			LOGGER.warn(stringBuilder.toString());
			order.setStatus(OrderStatus.CANCELLED);
			return order;
		}
	}

}
