package org.marceloleite.mercado.consumer.trades;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.marceloleite.mercado.commons.util.UnixTimeSeconds;
import org.marceloleite.mercado.consumer.AbstractConsumer;
import org.marceloleite.mercado.consumer.Consumer;
import org.marceloleite.mercado.consumer.model.Currency;
import org.marceloleite.mercado.consumer.model.JsonTrade;

public class TradesConsumer extends AbstractConsumer implements Consumer<List<JsonTrade>> {

	public TradesConsumer(Currency cryptocoin) {
		super(cryptocoin);
	}

	private static final String METHOD = "trades";

	public List<JsonTrade> consume(Object... args) {
		
		LocalDateTime from = null;
		if (args[0] instanceof LocalDateTime) {
			from = (LocalDateTime) args[0];
		}

		LocalDateTime to = null;
		if (args[1] instanceof LocalDateTime) {
			to = (LocalDateTime) args[1];
		
		}

		return Arrays.asList(createWebTarget().path(getPathWithParameters(from, to))
			.request(MediaType.APPLICATION_JSON)
			.get(JsonTrade[].class));
	}

	@Override
	protected String getMethod() {
		return METHOD;
	}

	private String getPathWithParameters(LocalDateTime from, LocalDateTime to) {
		return String.format(getPath() + "%d/%d/", new UnixTimeSeconds(from).get(), new UnixTimeSeconds(to).get());
	}

}
