package org.marceloleite.mercado.simulator.property;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("simulator")
public class SimulatorProperties {
	
	@NotBlank
	private String startTime;

	@NotBlank
	private String endTime;

	@NotBlank
	private Long stepDuration;

	@NotBlank
	private Long retrievingDuration;
	
	private Double tradeComissionPercentage;
	
	@NotBlank
	private Integer threadPoolSize;
	
	@NotBlank
	private Long tradeSiteDurationStep;
	
	@NotBlank
	private String persistencePropertiesFile;
	
	private String ignoreTradesOnDatabase;
	
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

	public Double getTradeComissionPercentage() {
		return tradeComissionPercentage;
	}

	public void setTradeComissionPercentage(Double tradeComissionPercentage) {
		this.tradeComissionPercentage = tradeComissionPercentage;
	}

	public Integer getThreadPoolSize() {
		return threadPoolSize;
	}

	public void setThreadPoolSize(Integer threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	public Long getTradeSiteDurationStep() {
		return tradeSiteDurationStep;
	}

	public void setTradeSiteDurationStep(Long tradeSiteDurationStep) {
		this.tradeSiteDurationStep = tradeSiteDurationStep;
	}

	public String getPersistencePropertiesFile() {
		return persistencePropertiesFile;
	}

	public void setPersistencePropertiesFile(String persistencePropertiesFile) {
		this.persistencePropertiesFile = persistencePropertiesFile;
	}

	public String getIgnoreTradesOnDatabase() {
		return ignoreTradesOnDatabase;
	}

	public void setIgnoreTradesOnDatabase(String ignoreTradesOnDatabase) {
		this.ignoreTradesOnDatabase = ignoreTradesOnDatabase;
	}
}
