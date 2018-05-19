package org.marceloleite.mercado.strategies.sixth.analyser;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import org.marceloleite.mercado.strategies.sixth.SixthStrategy;
import org.marceloleite.mercado.strategies.sixth.SixthStrategyStatus;

public class StatusAnalysers {

	private Map<SixthStrategyStatus, StatusAnalyser> statusAnalysers = new EnumMap<>(SixthStrategyStatus.class);

	public static Builder builder() {
		return new Builder();
	}

	private StatusAnalysers() {
	}

	private StatusAnalysers(Builder builder) {
		this.statusAnalysers = createStatusAnalysers(builder);
	}

	private Map<SixthStrategyStatus, StatusAnalyser> createStatusAnalysers(Builder builder) {
		Map<SixthStrategyStatus, StatusAnalyser> statusAnalysers = new EnumMap<>(SixthStrategyStatus.class);

		putOnStatusAnalysersMap(statusAnalysers, createUndefinedStatusAnalyser(builder));
		putOnStatusAnalysersMap(statusAnalysers, createAppliedStatusAnalyser(builder));
		putOnStatusAnalysersMap(statusAnalysers, createSavedStatusAnalyser(builder));

		return statusAnalysers;
	}

	private StatusAnalyser createUndefinedStatusAnalyser(Builder builder) {
		return UndefinedStatusAnalyser.builder()
				.strategy(builder.strategy)
				.build();
	}

	private StatusAnalyser createSavedStatusAnalyser(Builder builder) {
		return SavedStatusAnalyser.builder()
				.strategy(builder.strategy)
				.build();
	}

	private StatusAnalyser createAppliedStatusAnalyser(Builder builder) {
		return AppliedStatusAnalyser.builder()
				.strategy(builder.strategy)
				.build();
	}

	private void putOnStatusAnalysersMap(Map<SixthStrategyStatus, StatusAnalyser> statusAnalysers,
			StatusAnalyser statusAnalyser) {
		statusAnalysers.put(statusAnalyser.getStatus(), statusAnalyser);
	}

	public StatusAnalyser getStatusAnalyser(SixthStrategyStatus status) {

		if (status == null) {
			throw new RuntimeException("Status cannot be null.");
		}

		return Optional.ofNullable(statusAnalysers.get(status))
				.orElseThrow(
						() -> new RuntimeException("Could not find strategy for status \"" + status.getName() + "\"."));
	}

	public static class Builder {
		private SixthStrategy strategy;

		protected Builder() {
		};

		public Builder strategy(SixthStrategy strategy) {
			this.strategy = strategy;
			return this;
		}

		public StatusAnalysers build() {
			return new StatusAnalysers(this);
		}
	}
}
