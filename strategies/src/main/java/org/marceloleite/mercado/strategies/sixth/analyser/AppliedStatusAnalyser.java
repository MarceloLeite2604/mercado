package org.marceloleite.mercado.strategies.sixth.analyser;

import org.marceloleite.mercado.House;
import org.marceloleite.mercado.commons.Statistics;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.strategies.sixth.SixthStrategyStatus;

public class AppliedStatusAnalyser extends StatusAnalyserTemplate {

	public static Builder builder() {
		return new Builder();
	}

	private AppliedStatusAnalyser() {
	}

	private AppliedStatusAnalyser(Builder builder) {
		super(builder);
	}

	@Override
	public SixthStrategyStatus getStatus() {
		return SixthStrategyStatus.APPLIED;
	}

	@Override
	public Order analyse(TimeInterval timeInterval, Account account, House house) {
		TemporalTicker temporalTicker = house.getTemporalTickerFor(getStrategy().getCurrency());
		Statistics lastPriceStatistics = getStrategy().getStatistics()
				.getLastPriceStatistics();
		Statistics averagePriceStatistics = getStrategy().getStatistics()
				.getAveragePriceStatistics();
		Order result = null;

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(temporalTicker.getStart() + ": ");
		stringBuilder.append(" last ratio: " + lastPriceStatistics.getRatio() + " ");
		stringBuilder.append(" average variation: " + averagePriceStatistics.getVariation() + " ");
		stringBuilder.append(" threshold: " + (1 + getStrategy().getThresholds()
				.getShrinkPercentage()) + " ");
		LOGGER.debug(stringBuilder.toString());

		if (lastPriceStatistics.getRatio() > 1.0) {
			if (temporalTicker.getCurrentOrPreviousLast()
					.doubleValue() > lastPriceStatistics.getBase()) {
				updateGraphicAndBase(temporalTicker);
			}
		} else {
			if (lastPriceStatistics.getRatio() <= (1.0 + getStrategy().getThresholds()
					.getShrinkPercentage()) && averagePriceStatistics.getVariation() < 0) {
				updateGraphicAndBase(temporalTicker);
				result = createSellOrder(timeInterval, account, house);
			}
		}
		return result;
	}

	public static class Builder extends StatusAnalyserTemplate.Builder<Builder> {
		public AppliedStatusAnalyser build() {
			return new AppliedStatusAnalyser(this);
		}
	}
}
