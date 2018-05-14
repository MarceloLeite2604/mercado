package org.marceloleite.mercado.simulator.conversor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.model.TemporalTicker;

public final class TemporalTickersByCurrencyMapToTemporalTickersByTimeIntervalMapConversor {

	private TemporalTickersByCurrencyMapToTemporalTickersByTimeIntervalMapConversor() {

	}

	public static TreeMap<TimeInterval, Map<Currency, TemporalTicker>> convert(
			Map<Currency, List<TemporalTicker>> temporalTickersByCurrency) {
		TreeMap<TimeInterval, Map<Currency, TemporalTicker>> result = new TreeMap<>();

		for (Entry<Currency, List<TemporalTicker>> entry : temporalTickersByCurrency.entrySet()) {
			Currency currency = entry.getKey();
			List<TemporalTicker> temporalTickers = entry.getValue();
			for (TemporalTicker temporalTicker : temporalTickers) {
				result.getOrDefault(createTimeIntervalForTemporalTicker(temporalTicker), new HashMap<>())
						.putIfAbsent(currency, temporalTicker);
			}
		}
		return result;
	}

	private static TimeInterval createTimeIntervalForTemporalTicker(TemporalTicker temporalTicker) {
		return new TimeInterval(temporalTicker.getStart(), temporalTicker.getEnd());
	}
}
