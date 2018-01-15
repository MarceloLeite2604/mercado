package org.marceloleite.mercado.negotiationapi;

import java.util.List;

import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.commons.util.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;
import org.marceloleite.mercado.negotiationapi.getaccountinfo.GetAccountInfoMethod;
import org.marceloleite.mercado.negotiationapi.getaccountinfo.GetAccountInfoMethodResponse;
import org.marceloleite.mercado.negotiationapi.listorders.ListOrdersMethod;
import org.marceloleite.mercado.negotiationapi.listorders.ListOrdersMethodResponse;
import org.marceloleite.mercado.negotiationapi.listsystemmessages.ListSystemMessagesMethod;
import org.marceloleite.mercado.negotiationapi.listsystemmessages.ListSystemMessagesMethodResponse;
import org.marceloleite.mercado.negotiationapi.model.order.CurrencyPair;
import org.marceloleite.mercado.negotiationapi.model.systemmessage.SystemMessage;

public class Main {
	public static void main(String[] args) throws Exception {

		// listOrdersMethod();
		// listSystemMessagesMethod();
		getAccountInfo();
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
			List<SystemMessage> systemMessages = listSystemMessagesMethodResponse.getSystemMessages();
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
	private static void getAccountInfo() {
		try {
			GetAccountInfoMethodResponse getAccountInfoMethodResponse = new GetAccountInfoMethod().execute();
			System.out.println(new ObjectToJsonConverter().convertTo(getAccountInfoMethodResponse));
		} finally {
			EntityManagerController.getInstance().close();
		}
	}
}
