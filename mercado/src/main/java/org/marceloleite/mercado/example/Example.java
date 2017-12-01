package org.marceloleite.mercado.example;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class Example {
	public static void main(String[] args) {
		Quote quote = ClientBuilder.newClient()
			.target("https://gturnquist-quoters.cfapps.io/api/random")
			.request(MediaType.APPLICATION_JSON)
			.get(Quote.class);
		System.out.println(quote);
	}
}
