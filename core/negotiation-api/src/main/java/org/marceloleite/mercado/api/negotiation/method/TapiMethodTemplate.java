package org.marceloleite.mercado.api.negotiation.method;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.api.negotiation.util.EncryptionUtil;
import org.marceloleite.mercado.api.negotiation.util.NonceUtil;
import org.marceloleite.mercado.commons.utils.EncryptUtils;
import org.marceloleite.mercado.model.TapiInformation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class TapiMethodTemplate<T> {

	private static final Logger LOGGER = LogManager.getLogger(TapiMethodTemplate.class);

	private static final String TAPI_HOST = "www.mercadobitcoin.net";

	private static final String TAPI_PATH = "/tapi/v3/";

	private static final String TAPI_ID = "TAPI-ID";

	private static final String TAPI_MAC = "TAPI-MAC";

	private static final String PARAMETER_TAPI_METHOD = "tapi_method";

	private static final String PARAMETER_TAPI_NONCE = "tapi_nonce";

	private static final int MAXIMUM_RETRIES = 5;

	private TapiMethod tapiMethod;

	private String[] parameterNames;

	private TapiInformation tapiInformation;

	public TapiMethodTemplate(TapiInformation tapiInformation, TapiMethod tapiMethod, String[] parameterNames) {
		this.tapiInformation = tapiInformation;
		this.tapiMethod = tapiMethod;
		this.parameterNames = parameterNames;
	}

	public T executeMethod(Object... parameters) {
		URI uri = generateUriWithParameters(parameters);

		HttpHeaders httpHeaders = createHttpHeaders(uri);
		HttpEntity<Object> httpEntity = new HttpEntity<>(null, httpHeaders);
		Class<T> responseClass = retrieveResponseClass();

		RestTemplate restTemplate = new RestTemplate();
		int retries = 0;
		T response = null;
		while (response == null && retries < MAXIMUM_RETRIES) {
			ResponseEntity<T> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, responseClass);
			LOGGER.debug("HTTP method returned with status code " + responseEntity.getStatusCode() + " ("
					+ responseEntity.getStatusCode()
							.name()
					+ ").");

			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				response = responseEntity.getBody();
			} else {
				retries++;
			}
		}

		if (retries >= MAXIMUM_RETRIES) {
			LOGGER.error("Maximum retries reached. Aborting execution.");
			throw new RuntimeException("Could not connect to host \"" + uri + "\".");
		}

		return response;
	}

	private URI generateUriWithParameters(Object... parameters) {
		URI uri = generateUri(generateTapiParameters(parameters));
		LOGGER.debug("Url generated is: " + uri);
		return uri;
	}

	private TapiParameters generateTapiParameters(Object... objectParameters) {
		TapiParameters napiMethodParameters = new TapiParameters();
		napiMethodParameters.put(PARAMETER_TAPI_METHOD, tapiMethod);
		napiMethodParameters.put(PARAMETER_TAPI_NONCE, NonceUtil.getInstance()
				.next());

		if (parameterNames != null && parameterNames.length > 0) {
			if (objectParameters.length != parameterNames.length) {
				throw new RuntimeException("Method \"" + tapiMethod + "\" requires " + parameterNames.length
						+ " parameter(s) to be executed, but only " + objectParameters.length + " was(were) informed.");
			}

			int counter = 0;
			for (String parameterName : parameterNames) {
				napiMethodParameters.put(parameterName, objectParameters[counter++]);
			}
		}
		return napiMethodParameters;
	}

	private URI generateUri(TapiParameters napiParameters) {
		try {
			URIBuilder uriBuilder = new URIBuilder().setScheme("https")
					.setHost(TAPI_HOST)
					.setPath(TAPI_PATH);
			for (Entry<String, Object> napiParameter : napiParameters.entrySet()) {
				String name = napiParameter.getKey();
				Object value = napiParameter.getValue();
				if (value != null) {
					uriBuilder.addParameter(name, URLEncoder.encode(value.toString(), StandardCharsets.UTF_8.name()));
				}
			}
			return uriBuilder.build();
		} catch (UnsupportedEncodingException | URISyntaxException exception) {
			throw new RuntimeException("Error while elaborating NAPI URI method.", exception);
		}
	}

	private HttpHeaders createHttpHeaders(URI uri) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.add(TAPI_ID, tapiInformation.getIdentification());
		httpHeaders.add(TAPI_MAC, generateTapiMac(uri));
		return httpHeaders;
	}

	private String generateTapiMac(URI uri) {
		byte[] decryptedSecret = EncryptUtils.decrypt(tapiInformation.getSecret())
				.getBytes(StandardCharsets.UTF_8);
		return EncryptionUtil.getInstance()
				.generateTapiMac(uri, decryptedSecret);
	}

	@SuppressWarnings("unchecked")
	private Class<T> retrieveResponseClass() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
}
