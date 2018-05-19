package org.marceloleite.mercado.strategies.sixth.analyser;

import org.marceloleite.mercado.House;
import org.marceloleite.mercado.commons.Statistics;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.strategies.sixth.SixthStrategyStatus;

public class SavedStatusAnalyser extends StatusAnalyserTemplate {
	
	public static Builder builder() {
		return new Builder();
	}
	
	private SavedStatusAnalyser() {}

	private SavedStatusAnalyser(Builder builder) {
		super(builder);
	}
	
	@Override
	public SixthStrategyStatus getStatus() {
		return SixthStrategyStatus.UNDEFINED;
	}

	@Override
	public Order analyse(TimeInterval timeInterval, Account account, House house) {
		TemporalTicker temporalTicker = house.getTemporalTickerFor(getStrategy().getCurrency());
		Statistics lastPriceStatistics = getStrategy().getStatistics().getLastPriceStatistics();
		Order result = null;
		if (lastPriceStatistics.getRatio() < 0) {
			if (temporalTicker.getCurrentOrPreviousLast()
					.doubleValue() < lastPriceStatistics.getBase()) {
				updateGraphicAndBase(temporalTicker);
			}
		} else if (lastPriceStatistics.getRatio() >= getStrategy().getThresholds().getGrowthPercentage()
				&& getStrategy().getStatistics().getAveragePriceStatistics()
						.getVariation() > 0) {
			updateGraphicAndBase(temporalTicker);
			result = createBuyOrder(timeInterval, account, house);
		}
		return result;
	}

	public static class Builder extends StatusAnalyserTemplate.Builder<Builder> {
		public SavedStatusAnalyser build() {
			return new SavedStatusAnalyser(this);
		}		
	}

	
}
