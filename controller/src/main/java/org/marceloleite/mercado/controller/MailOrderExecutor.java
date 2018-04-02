package org.marceloleite.mercado.controller;

import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.Order;
import org.marceloleite.mercado.base.model.OrderExecutor;
import org.marceloleite.mercado.commons.OrderStatus;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.retriever.email.EmailMessage;

public class MailOrderExecutor implements OrderExecutor {

	@Override
	public Order placeOrder(Order order, House house, Account account) {
		StringBuilder subjectStringBuilder = new StringBuilder();
		subjectStringBuilder.append("Executing ");
		subjectStringBuilder.append(order.getType() == OrderType.BUY ? "buy " : "sell ");
		subjectStringBuilder.append("order");
		String subject = subjectStringBuilder.toString();
		String content = "Executing " + order;
		EmailMessage emailMessage = new EmailMessage();
		emailMessage.getToAddresses().add(account.getEmail());
		emailMessage.setSubject(subject);
		emailMessage.setContent(content);
		emailMessage.send();
		order.setStatus(OrderStatus.FILLED);
		return order;
	}

}
