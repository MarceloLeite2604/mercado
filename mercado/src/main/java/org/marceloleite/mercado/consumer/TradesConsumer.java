package org.marceloleite.mercado.consumer;

import java.util.Calendar;

import javax.ws.rs.core.MediaType;

import org.marceloleite.mercado.model.Cryptocoin;
import org.marceloleite.mercado.model.Trades;
import org.marceloleite.mercado.util.UnixTimeSeconds;

public class TradesConsumer extends AbstractConsumer implements Consumer<Trades[]> {

	public TradesConsumer(Cryptocoin cryptocoin) {
		super(cryptocoin);
	}

	private static final String METHOD = "trades";

	public Trades[] consume(Object... args) {

		Calendar from = null;
		if (args[0] instanceof Calendar) {
			from = (Calendar) args[0];
		}

		Calendar to = null;
		if (args[1] instanceof Calendar) {
			to = (Calendar) args[1];
		}

		return createWebTarget().path(getPathWithParameters(from, to))
			.request(MediaType.APPLICATION_JSON)
			.get(Trades[].class);
	}

	@Override
	protected String getMethod() {
		return METHOD;
	}

	private String getPathWithParameters(Calendar from, Calendar to) {
		return String.format(getPath() + "%d/%d/", new UnixTimeSeconds(from).get(), new UnixTimeSeconds(to).get());
	}

}
