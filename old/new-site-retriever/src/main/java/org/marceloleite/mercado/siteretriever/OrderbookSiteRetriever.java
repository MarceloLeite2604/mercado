package org.marceloleite.mercado.siteretriever;

import java.net.URI;
import java.net.URISyntaxException;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.model.Orderbook;
import org.springframework.web.client.RestTemplate;

public class OrderbookSiteRetriever extends AbstractSiteRetriever {

	private static final String METHOD = "orderbook";

	public Orderbook retrieve(Currency currency) {
		RestTemplate restTemplate = new RestTemplate();
		URI uri = elaborateURL(currency);
		return restTemplate.getForEntity(uri, Orderbook.class).getBody();
	}

	protected URI elaborateURL(Currency currency) {
		String path = String.format(BASE_PATH_TEMPLATE, currency, getMethod());
		try {
			return new URI(TARGET_URL + path);
		} catch (URISyntaxException exception) {
			throw new RuntimeException("Error while elaborating URI.", exception);
		}
	}

	@Override
	protected String getMethod() {
		return METHOD;
	}
}
