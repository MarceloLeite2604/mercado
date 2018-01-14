package org.marceloleite.mercado.negotiationapi.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpConnection {

	private static final Logger LOGGER = LogManager.getLogger(HttpConnection.class);

	private static final String TAPI_ID = "TAPI-ID";

	private static final String TAPI_MAC = "TAPI-MAC";

	private static final String CONTENT_TYPE = "Content-Type";

	private static final String CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded";

	public HttpsURLConnection createHttpsUrlConnection(URL url) {
		try {
			Map<String, Object> httpRequestProperties = new HashMap<>();
			httpRequestProperties.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
			httpRequestProperties.put(TAPI_ID, new EncryptionKey().retrieveKeyId());
			String tapiMac = generateTapiMac(url);
			LOGGER.debug("TAPI MAC is: " + tapiMac);
			httpRequestProperties.put(TAPI_MAC, tapiMac);
			return createHttpsRequestConnection(url, HttpMethod.POST, httpRequestProperties);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	private String generateTapiMac(URL url) throws UnsupportedEncodingException {
		return new Encryption().generateTapiMac(url, new EncryptionKey().retrieveKeySecret());
	}

	private static HttpsURLConnection createHttpsRequestConnection(URL url, HttpMethod httpMethod,
			Map<String, Object> properties) throws IOException {
		HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) url.openConnection();

		httpsUrlConnection.setRequestMethod(httpMethod.toString());

		if (properties != null && !properties.isEmpty()) {
			for (String propertyKey : properties.keySet()) {
				Object property = properties.get(propertyKey);
				httpsUrlConnection.setRequestProperty(propertyKey, property.toString());
			}
		}

		switch (httpMethod) {
		case GET:
			httpsUrlConnection.setDoOutput(false);
			break;
		case POST:
			httpsUrlConnection.setDoOutput(true);
			break;
		}
		
		return httpsUrlConnection;
	}

}
