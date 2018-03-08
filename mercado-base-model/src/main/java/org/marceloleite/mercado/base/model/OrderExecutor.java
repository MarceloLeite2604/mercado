package org.marceloleite.mercado.base.model;

import org.marceloleite.mercado.base.model.order.BuyOrderBuilder.BuyOrder;
import org.marceloleite.mercado.base.model.order.SellOrderBuilder.SellOrder;

public interface OrderExecutor {
	BuyOrder executeBuyOrder(BuyOrder buyOrder, House house, Account account);
	
	SellOrder executeSellOrder(SellOrder sellOrder, House house, Account account);
}
