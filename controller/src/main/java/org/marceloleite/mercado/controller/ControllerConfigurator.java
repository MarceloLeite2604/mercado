package org.marceloleite.mercado.controller;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.marceloleite.mercado.controller.properties.ControllerPropertiesRetriever;
import org.marceloleite.mercado.dao.mixed.TradeDatabaseSiteDAO;
import org.marceloleite.mercado.dao.site.siteretriever.trade.TradeSiteRetriever;
import org.springframework.stereotype.Component;

@Component
public class ControllerConfigurator {

	@Inject
	private ControllerPropertiesRetriever controllerPropertiesRetriever;

	@PostConstruct
	public void postConstruct() {
		TradeSiteRetriever.setConfiguredStepDuration(controllerPropertiesRetriever.retrieveTradeSiteDurationStep());
		TradeDatabaseSiteDAO.setIgnoreValuesOnDatabase(false);
	}
}
