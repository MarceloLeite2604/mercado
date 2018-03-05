package org.marceloleite.org.marceloleite.mercado.controller;

import java.util.Arrays;
import java.util.List;

import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.OrderExecutor;
import org.marceloleite.mercado.base.model.order.BuyOrderBuilder.BuyOrder;
import org.marceloleite.mercado.base.model.order.SellOrderBuilder.SellOrder;

public class MailOrderExecutor implements OrderExecutor {

	@Override
	public BuyOrder executeBuyOrder(BuyOrder buyOrder, House house, Account account) {
		List<String> to = Arrays.asList(account.getEmail());
		String subject = "Buy " + buyOrder.getCurrencyAmountToBuy().getCurrency();
		String content = "It's time to buy " + buyOrder.getCurrencyAmountToBuy() + ". You'll pay approximately "
				+ buyOrder.getCurrencyAmountToPay() + ".";
		new MailSender().sendEmail(to, null, null, subject, content);
		return buyOrder;
	}

	@Override
	public SellOrder executeSellOrder(SellOrder sellOrder, House house, Account account) {
		List<String> to = Arrays.asList(account.getEmail());
		String subject = "Sell " + sellOrder.getCurrencyAmountToSell().getCurrency();
		String content = "It's time to sell " + sellOrder.getCurrencyAmountToSell() + ". You'll receive approximately "
				+ sellOrder.getCurrencyAmountToReceive() + ".";
		new MailSender().sendEmail(to, null, null, subject, content);
		return sellOrder;
	}

}
