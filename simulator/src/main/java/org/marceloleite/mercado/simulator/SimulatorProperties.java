package org.marceloleite.mercado.simulator;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "simulator")
@PropertySource("classpath:simulator.properties")
public class SimulatorProperties {

	@NotBlank
	private String startTime;

	@NotBlank
	private String endTime;

	@NotBlank
	private Long stepDuration;

	@NotBlank
	private Long retrievingDuration;

	@NotBlank
	private Double tradePercentage;

	@NotBlank
	private String persistenceFile;

	private TradesSiteRetriever tradesSiteRetriever = new TradesSiteRetriever();

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Long getStepDuration() {
		return stepDuration;
	}

	public void setStepDuration(Long stepDuration) {
		this.stepDuration = stepDuration;
	}

	public Long getRetrievingDuration() {
		return retrievingDuration;
	}

	public void setRetrievingDuration(Long retrievingDuration) {
		this.retrievingDuration = retrievingDuration;
	}

	public Double getTradePercentage() {
		return tradePercentage;
	}

	public void setTradePercentage(Double tradePercentage) {
		this.tradePercentage = tradePercentage;
	}

	public String getPersistenceFile() {
		return persistenceFile;
	}

	public void setPersistenceFile(String persistenceFile) {
		this.persistenceFile = persistenceFile;
	}

	public TradesSiteRetriever getTradesSiteRetriever() {
		return tradesSiteRetriever;
	}

	public void setTradesSiteRetriever(TradesSiteRetriever tradesSiteRetriever) {
		this.tradesSiteRetriever = tradesSiteRetriever;
	}

	private static class TradesSiteRetriever {

		@NotBlank
		private Long threadPoolSize;

		@NotBlank
		private Long stepDuration;

		public Long getThreadPoolSize() {
			return threadPoolSize;
		}

		public void setThreadPoolSize(Long threadPoolSize) {
			this.threadPoolSize = threadPoolSize;
		}

		public Long getStepDuration() {
			return stepDuration;
		}

		public void setStepDuration(Long stepDuration) {
			this.stepDuration = stepDuration;
		}
	}

}
