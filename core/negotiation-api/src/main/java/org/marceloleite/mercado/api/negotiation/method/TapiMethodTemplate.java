package org.marceloleite.mercado.api.negotiation.method;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.api.negotiation.util.EncryptionUtil;
import org.marceloleite.mercado.commons.utils.EncryptUtils;
import org.marceloleite.mercado.commons.utils.creator.ObjectMapperCreator;
import org.marceloleite.mercado.model.TapiInformation;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public abstract class TapiMethodTemplate<T extends TapiResponse<?>> {

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

	private ParameterizedTypeReference<T> tapiResponseClass;

	public TapiMethodTemplate(TapiInformation tapiInformation, TapiMethod tapiMethod, String[] parameterNames,
			ParameterizedTypeReference<T> tapiResponseClass) {
		this.tapiInformation = tapiInformation;
		this.tapiMethod = tapiMethod;
		this.parameterNames = parameterNames;
		this.tapiResponseClass = tapiResponseClass;
	}

	public T executeMethod(Object... parameters) {
		// TODO This is a constant now.
		URI uri = generateUri();
		String requestBody = generateRequestBody(generateTapiParameters(parameters));

		HttpHeaders httpHeaders = createHttpHeaders(uri, requestBody);

		HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

		RestTemplate restTemplate = createRestTemplate();

		int retries = 0;
		T response = null;
		while (response == null && retries < MAXIMUM_RETRIES) {
			LOGGER.debug("Executing POST method on address \"" + uri + "\".");
			LOGGER.debug("HTTP request header: " + httpHeaders);
			LOGGER.debug("HTTP request body: " + requestBody);
			ResponseEntity<T> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, httpEntity,
					tapiResponseClass);
			// ResponseEntity<T> responseEntity = restTemplate.exchange(uri,
			// HttpMethod.POST, httpEntity, responseClass);
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

	private RestTemplate createRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
		messageConverter.setPrettyPrint(false);
		messageConverter.setObjectMapper(ObjectMapperCreator.create());
		restTemplate.getMessageConverters()
				.removeIf(m -> m.getClass()
						.getName()
						.equals(MappingJackson2HttpMessageConverter.class.getName()));
		restTemplate.getMessageConverters()
				.add(messageConverter);
		return restTemplate;
	}

	private String generateRequestBody(TapiParameters tapiParameters) {
		String result = new String("".getBytes(), StandardCharsets.UTF_8);
		for (Entry<String, Object> tapiParameter : tapiParameters.entrySet()) {
			String name = tapiParameter.getKey();
			Object value = tapiParameter.getValue();
			if (value != null) {
				if (!result.isEmpty()) {
					result += "&";
				}
				try {
					result += URLEncoder.encode(name, StandardCharsets.UTF_8.name()) + "="
							+ URLEncoder.encode(value.toString(), StandardCharsets.UTF_8.name());
				} catch (UnsupportedEncodingException exception) {
					throw new RuntimeException("Error while inserting \"" + value + "\" parameter on URl query for \""
							+ tapiMethod + "\" TAPI method.", exception);
				}
			}
		}
		return result;
	}

	private TapiParameters generateTapiParameters(Object... objectParameters) {
		TapiParameters napiMethodParameters = new TapiParameters();
		napiMethodParameters.put(PARAMETER_TAPI_METHOD, tapiMethod);
		napiMethodParameters.put(PARAMETER_TAPI_NONCE, generateNonceValue());

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

	private long generateNonceValue() {
		return System.currentTimeMillis() / 1000L;
	}

	private URI generateUri() {
		try {
			URI uri = new URIBuilder().setScheme("https")
					.setHost(TAPI_HOST)
					.setPath(TAPI_PATH)
					.build();
			LOGGER.debug("URI generated is: " + uri);
			return uri;
		} catch (URISyntaxException exception) {
			throw new RuntimeException("Error while elaborating TAPI URI for \"" + tapiMethod + "\" method.",
					exception);
		}
	}

	private HttpHeaders createHttpHeaders(URI uri, String requestBody) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.add(TAPI_ID, decryptTapiId());
		httpHeaders.add(TAPI_MAC, generateTapiMac(uri, requestBody));
		return httpHeaders;
	}

	private String decryptTapiId() {
		return EncryptUtils.decrypt(tapiInformation.getIdentification());
	}

	private String generateTapiMac(URI uri, String requestBody) {
		byte[] decryptedSecret = EncryptUtils.decrypt(tapiInformation.getSecret())
				.getBytes(StandardCharsets.UTF_8);
		return EncryptionUtil.generateTapiMac(uri, requestBody, decryptedSecret);
	}
}
