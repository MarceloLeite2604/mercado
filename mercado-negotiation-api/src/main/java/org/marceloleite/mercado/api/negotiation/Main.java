package org.marceloleite.mercado.api.negotiation;

import java.util.List;

import org.marceloleite.mercado.api.negotiation.methods.getaccountinfo.GetAccountInfoMethod;
import org.marceloleite.mercado.api.negotiation.methods.getaccountinfo.GetAccountInfoMethodResponse;
import org.marceloleite.mercado.api.negotiation.methods.getorder.GetOrderMethod;
import org.marceloleite.mercado.api.negotiation.methods.getorder.GetOrderMethodResponse;
import org.marceloleite.mercado.api.negotiation.methods.listorderbook.ListOrderbookMethod;
import org.marceloleite.mercado.api.negotiation.methods.listorderbook.ListOrderbookMethodResponse;
import org.marceloleite.mercado.api.negotiation.methods.listorders.ListOrdersMethod;
import org.marceloleite.mercado.api.negotiation.methods.listorders.ListOrdersMethodResponse;
import org.marceloleite.mercado.api.negotiation.methods.listsystemmessages.ListSystemMessagesMethod;
import org.marceloleite.mercado.api.negotiation.methods.listsystemmessages.ListSystemMessagesMethodResponse;
import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.commons.util.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;
import org.marceloleite.mercado.negotiationapi.model.CurrencyPair;
import org.marceloleite.mercado.negotiationapi.model.listsystemmessages.SystemMessage;

public class Main {
	public static void main(String[] args) throws Exception {

		listOrdersMethod();
		// listSystemMessagesMethod();
		// getAccountInfoMethod();
		// getOrderMethod();
		// listOrderbookMethod();
	}

	@SuppressWarnings("unused")
	private static void listOrderbookMethod() {
		try {
			ListOrderbookMethodResponse listOrderbookMethodResponse = new ListOrderbookMethod()
					.execute(CurrencyPair.BRLBCH);
			System.out.println(new ObjectToJsonConverter().convertTo(listOrderbookMethodResponse.getResponse()));
		} finally {
			EntityManagerController.getInstance().close();
		}
	}

	@SuppressWarnings("unused")
	private static void listSystemMessagesMethod() {
		try {
			ListSystemMessagesMethodResponse listSystemMessagesMethodResponse = new ListSystemMessagesMethod()
					.execute();
			System.out.println("Status code: " + listSystemMessagesMethodResponse.getStatusCode());
			System.out.println("Error message: " + listSystemMessagesMethodResponse.getErrorMessage());
			System.out.println("Timestamp: "
					+ new LocalDateTimeToStringConverter().convertTo(listSystemMessagesMethodResponse.getTimestamp()));
			List<SystemMessage> systemMessages = listSystemMessagesMethodResponse.getResponse();
			System.out.println("Total messages: " + systemMessages.size());
			if (!systemMessages.isEmpty()) {
				for (SystemMessage systemMessage : systemMessages) {
					System.out.println("\t Event code: " + systemMessage.getEventCode());
					System.out.println("\t Message content: " + systemMessage.getMessageContent());
				}
			}
		} finally {
			EntityManagerController.getInstance().close();
		}
	}

	@SuppressWarnings("unused")
	private static void listOrdersMethod() {
		try {
			ListOrdersMethodResponse listOrdersMethodResponse = new ListOrdersMethod().execute(CurrencyPair.BRLBCH);
			System.out.println(new ObjectToJsonConverter().convertTo(listOrdersMethodResponse));
		} finally {
			EntityManagerController.getInstance().close();
		}
	}

	@SuppressWarnings("unused")
	private static void getAccountInfoMethod() {
		try {
			GetAccountInfoMethodResponse getAccountInfoMethodResponse = new GetAccountInfoMethod().execute();
			System.out.println(new ObjectToJsonConverter().convertTo(getAccountInfoMethodResponse.getResponse()));
		} finally {
			EntityManagerController.getInstance().close();
		}
	}

	@SuppressWarnings("unused")
	private static void getOrderMethod() {
		try {
			GetOrderMethodResponse getOrderMethodResponse = new GetOrderMethod().execute(CurrencyPair.BRLBCH, 1024453l);
			System.out.println(new ObjectToJsonConverter().convertTo(getOrderMethodResponse.getResponse()));
		} finally {
			EntityManagerController.getInstance().close();
		}
	}
}
