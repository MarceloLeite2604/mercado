package org.marceloleite.mercado.controller;

import org.marceloleite.mercado.api.negotiation.methods.placebuyorder.PlaceBuyOrderMethod;
import org.marceloleite.mercado.api.negotiation.methods.placebuyorder.PlaceBuyOrderMethodResponse;
import org.marceloleite.mercado.api.negotiation.methods.placesellorder.PlaceSellOrderMethod;
import org.marceloleite.mercado.api.negotiation.methods.placesellorder.PlaceSellOrderMethodResult;
import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.NewOrderExecutor;
import org.marceloleite.mercado.base.model.Order;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderStatus;
import org.marceloleite.mercado.data.OrderData;
import org.marceloleite.mercado.negotiationapi.model.CurrencyPair;
import org.marceloleite.mercado.negotiationapi.model.placebuyorder.PlaceBuyOrderResponse;
import org.marceloleite.mercado.negotiationapi.model.placesellorder.PlaceSellOrderResult;

public class NewControllerOrderExecutor implements NewOrderExecutor {

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
		Double quantity = order.getQuantity();
		Double limitPrice = order.getLimitPrice();
		
		PlaceSellOrderMethod placeSellOrderMethod = new PlaceSellOrderMethod(account.getTapiInformation());
		PlaceSellOrderMethodResult placeSellOrderMethodResponse = placeSellOrderMethod.execute(currencyPair, quantity, limitPrice);
		if ( placeSellOrderMethodResponse.getStatusCode() == 100 ) {
			PlaceSellOrderResult placeSellOrderResponse = placeSellOrderMethodResponse.getResponse();
			OrderData orderData = placeSellOrderResponse.getOrderData();
			return new Order(orderData);
		} else {
			order.setStatus(OrderStatus.CANCELLED);
			return order;
		}
	}

	private Order placeBuyOrder(Order order, Account account) {
		PlaceBuyOrderMethod placeBuyOrderMethod = new PlaceBuyOrderMethod(account.getTapiInformation());
		CurrencyPair currencyPair = CurrencyPair.retrieveByPair(order.getFirstCurrency(), order.getSecondCurrency());
		double quantity = order.getQuantity();
		double limitPrice = order.getLimitPrice();
		PlaceBuyOrderMethodResponse placeBuyOrderMethodResponse = placeBuyOrderMethod.execute(currencyPair, quantity, limitPrice);
		if ( placeBuyOrderMethodResponse.getStatusCode() == 100 ) {
			PlaceBuyOrderResponse placeBuyOrderResponse = placeBuyOrderMethodResponse.getResponse();
			OrderData orderData = placeBuyOrderResponse.getOrderData();
			return new Order(orderData);
		} else {
			order.setStatus(OrderStatus.CANCELLED);
			return order;
		}
	}

}
