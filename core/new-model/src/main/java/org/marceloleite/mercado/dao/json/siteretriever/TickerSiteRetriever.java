package org.marceloleite.mercado.dao.json.siteretriever;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.model.Ticker;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;

public class TickerSiteRetriever extends AbstractSiteRetriever {

	private static final String METHOD = "ticker";

	public Ticker retrieve(Currency currency) {
		RestTemplate restTemplate = new RestTemplate();
		URI uri = elaborateURL(currency);
		ParameterizedTypeReference<HashMap<String, Ticker>> responseType = createParameterizedTypeReference();

		RequestEntity<Void> request = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).build();

		Map<String, Ticker> response = restTemplate.exchange(request, responseType).getBody();

		Ticker ticker = retrieveTicker(response);
		ticker.setCurrency(currency);
		return ticker;
	}

	private Ticker retrieveTicker(Map<String, Ticker> response) {
		if (response == null || response.isEmpty()) {
			throw new RuntimeException("Error while retrieving a ticker. Ticker retriever received an empty result.");
		}

		String key = response.keySet().stream().findFirst().orElseThrow(() -> new RuntimeException(
				"Error while retrieving a ticker. Could not find first element on method response."));
			Ticker ticker = response.get(key);
		return ticker;
	}

	private ParameterizedTypeReference<HashMap<String, Ticker>> createParameterizedTypeReference() {
		ParameterizedTypeReference<HashMap<String, Ticker>> responseType = new ParameterizedTypeReference<HashMap<String, Ticker>>() {
		};
		return responseType;
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
