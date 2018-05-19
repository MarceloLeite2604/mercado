package org.marceloleite.mercado.strategies.sixth.parameter;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.Map;

import org.marceloleite.mercado.strategies.sixth.SixthStrategyStatistics;
import org.marceloleite.mercado.strategies.sixth.SixthStrategyStatus;
import org.marceloleite.mercado.strategies.sixth.SixthStrategyThresholds;
import org.marceloleite.mercado.strategies.sixth.graphic.SixthStrategyGraphic;

public class SixthStrategyParametersReader {

	private SixthStrategyThresholds sixthStrategyThresholds;

	private SixthStrategyStatistics sixthStrategyStatistics;

	private SixthStrategyGraphic sixthStrategyGraphic;

	private Map<SixthStrategyParameter, Object> parameters = new EnumMap<>(SixthStrategyParameter.class);

	public SixthStrategyThresholds getSixthStrategyThresholds() {
		return sixthStrategyThresholds;
	}

	public SixthStrategyStatistics getSixthStrategyStatistics() {
		return sixthStrategyStatistics;
	}

	public BigDecimal getWorkingAmountCurrency() {
		Double value = getParameterValue(SixthStrategyParameter.WORKING_AMOUNT_CURRENCY);
		return new BigDecimal(value);
	}

	public SixthStrategyStatus getStatus() {
		return getParameterValue(SixthStrategyParameter.INITIAL_STATUS);
	}

	public LocalTime getDailyGraphicTime() {
		return getParameterValue(SixthStrategyParameter.DAILY_GRAPHIC_TIME);
	}

	public Boolean getCreateDailyGraphic() {
		return getParameterValue(SixthStrategyParameter.CREATE_DAILY_GRAPHIC);
	}

	public Boolean getCreateGraphicAtEndOfExecution() {
		return getParameterValue(SixthStrategyParameter.CREATE_GRAPHIC_AT_END_OF_EXECUTION);
	}

	public SixthStrategyGraphic getSixthStrategyGraphic() {
		return sixthStrategyGraphic;
	}

	public void setParameter(String name, Object object) {
		parameters.put(SixthStrategyParameter.findByName(name), object);
		createObjects();
	}

	private void createObjects() {
		if (sixthStrategyStatistics == null) {
			sixthStrategyStatistics = createStatistics();
		}

		if (sixthStrategyThresholds == null) {
			sixthStrategyThresholds = createThresholds();
		}

		if (sixthStrategyGraphic == null) {
			sixthStrategyGraphic = createGraphic();
		}
	}

	private SixthStrategyThresholds createThresholds() {
		SixthStrategyThresholds sixthStrategyThresholds = null;
		Double shrinkPercentageThreshold = getParameterValue(SixthStrategyParameter.SHRINK_PERCENTAGE_THRESHOLD);
		Double growthPercentageThreshold = getParameterValue(SixthStrategyParameter.GROWTH_PERCENTAGE_THRESHOLD);
		if (shrinkPercentageThreshold != null && growthPercentageThreshold != null) {
			sixthStrategyThresholds = SixthStrategyThresholds.builder()
					.growthPercentage(growthPercentageThreshold)
					.shrinkPercentage(shrinkPercentageThreshold)
					.build();
		}
		return sixthStrategyThresholds;
	}

	@SuppressWarnings("unchecked")
	private <T> T getParameterValue(SixthStrategyParameter sixthStrategyParameter) {
		return (T) parameters.get(sixthStrategyParameter);
	}

	private SixthStrategyStatistics createStatistics() {
		SixthStrategyStatistics sixthStrategyStatistics = null;
		Integer circularArraySize = getParameterValue(SixthStrategyParameter.CIRCULAR_ARRAY_SIZE);
		Integer nextValueSteps = getParameterValue(SixthStrategyParameter.NEXT_VALUE_STEPS);
		if (circularArraySize != null && nextValueSteps != null) {

			sixthStrategyStatistics = SixthStrategyStatistics.builder()
					.circularArraySize(circularArraySize)
					.nextValueSteps(nextValueSteps)
					.build();
		}
		return sixthStrategyStatistics;
	}

	private SixthStrategyGraphic createGraphic() {
		SixthStrategyGraphic sixStrategyGraphic = null;
		if (willCreateGraphic() && sixthStrategyStatistics != null && sixthStrategyStatistics != null) {
			sixStrategyGraphic = new SixthStrategyGraphic(sixthStrategyStatistics, sixthStrategyThresholds);
		}
		return sixStrategyGraphic;
	}

	private Boolean willCreateGraphic() {
		return (willCreateDailyGraphic() || willCreateGraphicAtEndOfExecution());
	}

	private boolean willCreateDailyGraphic() {
		Boolean createDailyGraphic = getParameterValue(SixthStrategyParameter.CREATE_DAILY_GRAPHIC);
		return createDailyGraphic != null && createDailyGraphic;
	}

	private boolean willCreateGraphicAtEndOfExecution() {
		Boolean createGraphicAtEndOfExecution = getParameterValue(
				SixthStrategyParameter.CREATE_GRAPHIC_AT_END_OF_EXECUTION);
		return createGraphicAtEndOfExecution != null && createGraphicAtEndOfExecution;
	}
}
