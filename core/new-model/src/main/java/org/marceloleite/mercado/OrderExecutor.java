package org.marceloleite.mercado;

import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Order;

public interface OrderExecutor {
	Order placeOrder(Order order, House house, Account account);
}
