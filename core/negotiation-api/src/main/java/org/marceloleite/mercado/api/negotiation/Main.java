package org.marceloleite.mercado.api.negotiation;

import org.marceloleite.mercado.CurrencyPair;
import org.marceloleite.mercado.api.negotiation.method.GetAccountInfo;
import org.marceloleite.mercado.api.negotiation.method.GetOrder;
import org.marceloleite.mercado.api.negotiation.method.ListOrderbook;
import org.marceloleite.mercado.api.negotiation.method.ListOrders;
import org.marceloleite.mercado.api.negotiation.method.ListSystemMessages;
import org.marceloleite.mercado.api.negotiation.method.TapiResponse;
import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.model.TapiInformation;

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
		printResponse(new ListOrderbook(createTapiInformation()).execute(CurrencyPair.BRLBCH));
	}

	@SuppressWarnings("unused")
	private static void listSystemMessagesMethod() {
		printResponse(new ListSystemMessages(createTapiInformation())
					.execute());		
	}

	@SuppressWarnings("unused")
	private static void listOrdersMethod() {
		printResponse(new ListOrders(createTapiInformation()).execute(CurrencyPair.BRLBCH));
	}

	@SuppressWarnings("unused")
	private static void getAccountInfoMethod() {
		printResponse(new GetAccountInfo(createTapiInformation()).execute());
	}

	@SuppressWarnings("unused")
	private static void getOrderMethod() {
		printResponse(new GetOrder(createTapiInformation()).execute(CurrencyPair.BRLBCH, 1024453L));
	}

	private static TapiInformation createTapiInformation() {
		TapiInformation tapiInformation = new TapiInformation();
		tapiInformation.setIdentification("");
		tapiInformation.setSecret("");
		return tapiInformation;
	}
	
	private static void printResponse(TapiResponse<?> tapiResponse) {
		System.out.println(new ObjectToJsonConverter().convertTo(tapiResponse));
	}	
}
