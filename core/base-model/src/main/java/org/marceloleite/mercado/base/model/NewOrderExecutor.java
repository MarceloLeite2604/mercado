package org.marceloleite.mercado.base.model;

public interface NewOrderExecutor {
	Order placeOrder(Order order, House house, Account account);
}
