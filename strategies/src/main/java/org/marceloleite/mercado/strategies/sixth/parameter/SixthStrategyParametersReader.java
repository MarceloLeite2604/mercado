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

	private BigDecimal workingAmountCurrency;

	private SixthStrategyStatus status;

	private SixthStrategyGraphic sixthStrategyGraphic;

	private Map<SixthStrategyParameter, Object> parameters = new EnumMap<>(SixthStrategyParameter.class);

	public SixthStrategyThresholds getSixthStrategyThresholds() {
		return sixthStrategyThresholds;
	}

	public void setSixthStrategyThresholds(SixthStrategyThresholds sixthStrategyThresholds) {
		this.sixthStrategyThresholds = sixthStrategyThresholds;
	}

	public SixthStrategyStatistics getSixthStrategyStatistics() {
		return sixthStrategyStatistics;
	}

	public void setSixthStrategyStatistics(SixthStrategyStatistics sixthStrategyStatistics) {
		this.sixthStrategyStatistics = sixthStrategyStatistics;
	}

	public BigDecimal getWorkingAmountCurrency() {
		return workingAmountCurrency;
	}

	public void setWorkingAmountCurrency(BigDecimal workingAmountCurrency) {
		this.workingAmountCurrency = workingAmountCurrency;
	}

	public SixthStrategyStatus getStatus() {
		return status;
	}

	public LocalTime getDailyGraphicTime() {
		return getParameterValue(SixthStrategyParameter.DAILY_GRAPHIC_TIME);
	}

	public Boolean getCreateDailyGraphic() {
		return getParameterValue(SixthStrategyParameter.CREATE_DAILY_GRAPHIC);
	}

	public void setStatus(SixthStrategyStatus status) {
		this.status = status;
	}

	public SixthStrategyGraphic getSixthStrategyGraphic() {
		return sixthStrategyGraphic;
	}

	public void setSixthStrategyGraphic(SixthStrategyGraphic sixthStrategyGraphic) {
		this.sixthStrategyGraphic = sixthStrategyGraphic;
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

		if (parameters.get(SixthStrategyParameter.DAILY_GRAPHIC_TIME) != null) {
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
		Boolean createDailyGraphic = getParameterValue(SixthStrategyParameter.CREATE_DAILY_GRAPHIC);
		if (createDailyGraphic != null && createDailyGraphic && sixthStrategyStatistics != null
				&& sixthStrategyStatistics != null) {
			sixStrategyGraphic = new SixthStrategyGraphic(sixthStrategyStatistics, sixthStrategyThresholds);
		}
		return sixStrategyGraphic;
	}

	public boolean doneReading() {
		return (sixthStrategyThresholds != null && sixthStrategyStatistics != null && workingAmountCurrency != null
				&& status != null && sixthStrategyGraphic != null);
	}
}
