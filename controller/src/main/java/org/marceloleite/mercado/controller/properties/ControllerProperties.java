package org.marceloleite.mercado.controller.properties;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("controller")
public class ControllerProperties {
	
	@NotBlank
	private Integer threadPoolSize;
	
	@NotBlank
	private Long tradeSiteDurationStep;
	
	@NotBlank
	private String persistencePropertiesFile;
	
	private String ignoreTradesOnDatabase;
	
	private Double tradeComissionPercentage;
	
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

	public Double getTradeComissionPercentage() {
		return tradeComissionPercentage;
	}

	public void setTradeComissionPercentage(Double tradeComissionPercentage) {
		this.tradeComissionPercentage = tradeComissionPercentage;
	}
}
