package org.marceloleite.mercado.api.negotiation;

import java.util.List;

import org.marceloleite.mercado.api.negotiation.methods.GetAccountInfo;
import org.marceloleite.mercado.api.negotiation.methods.GetAccountInfo;
import org.marceloleite.mercado.api.negotiation.methods.GetOrder;
import org.marceloleite.mercado.api.negotiation.methods.GetOrder;
import org.marceloleite.mercado.api.negotiation.methods.ListOrderbook;
import org.marceloleite.mercado.api.negotiation.methods.ListOrderbook;
import org.marceloleite.mercado.api.negotiation.methods.ListOrders;
import org.marceloleite.mercado.api.negotiation.methods.ListOrders;
import org.marceloleite.mercado.api.negotiation.methods.ListSystemMessages;
import org.marceloleite.mercado.api.negotiation.methods.ListSystemMessages;
import org.marceloleite.mercado.base.model.TapiInformation;
import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;
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
			ListOrderbookMethodResponse listOrderbookMethodResponse = new ListOrderbook(createTapiInformation())
					.execute(CurrencyPair.BRLBCH);
			System.out.println(new ObjectToJsonConverter().convertTo(listOrderbookMethodResponse.getResponse()));
		} finally {
			EntityManagerController.getInstance().close();
		}
	}

	@SuppressWarnings("unused")
	private static void listSystemMessagesMethod() {
		try {
			ListSystemMessagesMethodResponse listSystemMessagesMethodResponse = new ListSystemMessages(createTapiInformation())
					.execute();
			System.out.println("Status code: " + listSystemMessagesMethodResponse.getStatusCode());
			System.out.println("Error message: " + listSystemMessagesMethodResponse.getErrorMessage());
			System.out.println("Timestamp: "
					+ ZonedDateTimeToStringConverter.getInstance().convertTo(listSystemMessagesMethodResponse.getTimestamp()));
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
			ListOrdersMethodResponse listOrdersMethodResponse = new ListOrders(createTapiInformation()).execute(CurrencyPair.BRLBCH);
			System.out.println(new ObjectToJsonConverter().convertTo(listOrdersMethodResponse));
		} finally {
			EntityManagerController.getInstance().close();
		}
	}

	@SuppressWarnings("unused")
	private static void getAccountInfoMethod() {
		try {
			GetAccountInfoMethodResponse getAccountInfoMethodResponse = new GetAccountInfo(createTapiInformation()).execute();
			System.out.println(new ObjectToJsonConverter().convertTo(getAccountInfoMethodResponse.getResponse()));
		} finally {
			EntityManagerController.getInstance().close();
		}
	}

	@SuppressWarnings("unused")
	private static void getOrderMethod() {
		try {
			GetOrderMethodResponse getOrderMethodResponse = new GetOrder(createTapiInformation()).execute(CurrencyPair.BRLBCH, 1024453l);
			System.out.println(new ObjectToJsonConverter().convertTo(getOrderMethodResponse.getResponse()));
		} finally {
			EntityManagerController.getInstance().close();
		}
	}
	
	private static TapiInformation createTapiInformation() {
		TapiInformation tapiInformation = new TapiInformation();
		tapiInformation.setId("z8IetMpskqZLuNipdAuAp4jb16axbxeIrKy5gI33c6tNJwilFUzwDw==");
		tapiInformation.setSecret("avR+lJKCyZSQaScxSll9dM7T1TnYzI9gTUfqEWXfgCdnrrhowyJTmveDygS3kcG6iVGgg9Hd0Ar/JHp+p2HiJKjmYhGJZbdW");
		return tapiInformation;
	}
}
