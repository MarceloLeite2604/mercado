package org.marceloleite.mercado.siteretriever.trades;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.interfaces.Retriever;
import org.marceloleite.mercado.commons.util.UnixTimeSeconds;
import org.marceloleite.mercado.siteretriever.AbstractSiteRetriever;
import org.marceloleite.mercado.siteretriever.model.JsonTrade;

public class TradesSiteRetriever extends AbstractSiteRetriever implements Retriever<List<JsonTrade>> {

	public TradesSiteRetriever(Currency currency) {
		super(currency);
	}

	private static final String METHOD = "trades";

	public List<JsonTrade> retrieve(Object... args) {
		
		LocalDateTime from = null;
		if (args[0] instanceof LocalDateTime) {
			from = (LocalDateTime) args[0];
		}

		LocalDateTime to = null;
		if (args[1] instanceof LocalDateTime) {
			to = (LocalDateTime) args[1];
		
		}

		List<JsonTrade> jsonTrades = Arrays.asList(createWebTarget().path(getPathWithParameters(from, to))
			.request(MediaType.APPLICATION_JSON)
			.get(JsonTrade[].class));
		jsonTrades.stream().forEach(jsonTrade -> jsonTrade.setCurrency(currency));
		return jsonTrades;
	}

	@Override
	protected String getMethod() {
		return METHOD;
	}

	private String getPathWithParameters(LocalDateTime from, LocalDateTime to) {
		return String.format(getPath() + "%d/%d/", new UnixTimeSeconds(from).get(), new UnixTimeSeconds(to).get());
	}

}
