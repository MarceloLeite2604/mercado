package org.marceloleite.mercado.siteretriever;

public abstract class AbstractSiteRetriever {

	protected static final String TARGET_URL = "https://www.mercadobitcoin.net";

	protected static final String BASE_PATH_TEMPLATE = "/api/%s/%s/";
	
	protected abstract String getMethod();
}
