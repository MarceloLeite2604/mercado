package org.marceloleite.mercado.negotiationapi;

public abstract class AbstractTapiMethod {
	
	private static final String DOMAIN_ADDRESS = "https://www.mercadobitcoin.net";

	private static final String TAPI_PATH = "/tapi/v3/";

	protected static final String PARAMETER_TAPI_METHOD = "tapi_method";

	protected static final String PARAMETER_TAPI_NONCE = "tapi_nonce";
	
	protected static final int STATUS_OK = 100;

	public TapiMethodParameters generateTapiMethodParameters() {
		TapiMethodParameters tapiMethodParameters = new TapiMethodParameters();
		tapiMethodParameters.put(PARAMETER_TAPI_METHOD, getTapiMethod());
		tapiMethodParameters.put(PARAMETER_TAPI_NONCE, NonceGenerator.getInstance().nextNonce());
		return tapiMethodParameters;
	}

	protected abstract TapiMethod getTapiMethod();

	public String generateAddress() {
		return DOMAIN_ADDRESS + TAPI_PATH;
	}
}
