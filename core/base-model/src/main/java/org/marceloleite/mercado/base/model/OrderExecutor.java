package org.marceloleite.mercado.base.model;

public interface OrderExecutor {
	Order placeOrder(Order order, House house, Account account);
}
