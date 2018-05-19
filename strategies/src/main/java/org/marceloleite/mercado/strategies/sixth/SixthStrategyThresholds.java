package org.marceloleite.mercado.strategies.sixth;

public class SixthStrategyThresholds {

	private double growthPercentage;

	private double shrinkPercentage;

	private SixthStrategyThresholds(Builder builder) {
		this.growthPercentage = builder.growthPercentage;
		this.shrinkPercentage = builder.shrinkPercentage;
	}

	public double getGrowthPercentage() {
		return growthPercentage;
	}

	public double getShrinkPercentage() {
		return shrinkPercentage;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private double growthPercentage;

		private double shrinkPercentage;

		private Builder() {
		}

		public Builder growthPercentage(double growthPercentage) {
			this.growthPercentage = growthPercentage;
			return this;
		}

		public Builder shrinkPercentage(double shrinkPercentage) {
			this.shrinkPercentage = shrinkPercentage;
			return this;
		}

		public SixthStrategyThresholds build() {
			return new SixthStrategyThresholds(this);
		}
	}
}
