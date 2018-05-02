package org.marceloleite.mercado.controller;

import org.marceloleite.mercado.House;
import org.marceloleite.mercado.OrderExecutor;
import org.marceloleite.mercado.commons.OrderStatus;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.email.EmailMessage;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Order;

public class MailOrderExecutor implements OrderExecutor {

	private static MailOrderExecutor instance;

	private MailOrderExecutor() {
	}

	@Override
	public Order placeOrder(Order order, House house, Account account) {
		StringBuilder subjectStringBuilder = new StringBuilder();
		subjectStringBuilder.append("Executing ");
		subjectStringBuilder.append(order.getType() == OrderType.BUY ? "buy " : "sell ");
		subjectStringBuilder.append("order");
		String subject = subjectStringBuilder.toString();
		String content = "Executing " + order;
		EmailMessage emailMessage = new EmailMessage();
		emailMessage.getToAddresses()
				.add(account.getEmail());
		emailMessage.setSubject(subject);
		emailMessage.setContent(content);
		emailMessage.send();
		order.setStatus(OrderStatus.FILLED);
		return order;
	}

	public static MailOrderExecutor getInstance() {
		if (instance == null) {
			instance = new MailOrderExecutor();
		}
		return instance;
	}

}
