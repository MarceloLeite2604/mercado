package org.marceloleite.mercado.controller;

import java.util.Arrays;
import java.util.List;

import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.Order;
import org.marceloleite.mercado.base.model.OrderExecutor;
import org.marceloleite.mercado.commons.MailSender;
import org.marceloleite.mercado.commons.OrderStatus;
import org.marceloleite.mercado.commons.OrderType;

public class MailOrderExecutor implements OrderExecutor {

	@Override
	public Order placeOrder(Order order, House house, Account account) {
		List<String> to = Arrays.asList(account.getEmail());
		StringBuilder subjectStringBuilder = new StringBuilder();
		subjectStringBuilder.append("Executing ");
		subjectStringBuilder.append(order.getType() == OrderType.BUY ? "buy " : "sell ");
		subjectStringBuilder.append("order");
		String subject = subjectStringBuilder.toString();
		String content = "Executing " + order;
		new MailSender().sendEmail(to, null, null, subject, content);
		order.setStatus(OrderStatus.FILLED);
		return order;
	}

}
