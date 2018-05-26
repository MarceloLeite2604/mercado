package org.marceloleite.mercado.controller;

import org.marceloleite.mercado.House;
import org.marceloleite.mercado.OrderExecutor;
import org.marceloleite.mercado.commons.OrderStatus;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.email.EmailMessage;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Order;
import org.springframework.stereotype.Component;

@Component
public class MailOrderExecutor implements OrderExecutor {

	@Override
	public Order placeOrder(Order order, House house, Account account) {
		EmailMessage emailMessage = elaborateEmail(account, order);
		emailMessage.send();
		order.setStatus(OrderStatus.FILLED);
		return order;
	}

	private EmailMessage elaborateEmail(Account account, Order order) {
		String subject = elaborateSubject(order);
		String content = "Executing " + order;
		EmailMessage emailMessage = new EmailMessage();
		emailMessage.getToAddresses()
				.add(account.getEmail());
		emailMessage.setSubject(subject);
		emailMessage.setContent(content);
		return emailMessage;
	}

	private String elaborateSubject(Order order) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Executing ");
		stringBuilder.append(order.getType() == OrderType.BUY ? "buy " : "sell ");
		stringBuilder.append("order");
		String subject = stringBuilder.toString();
		return subject;
	}

}
