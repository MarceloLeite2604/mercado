package org.marceloleite.mercado.api.negotiation.methods;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.api.negotiation.methods.response.NapiResponse;
import org.marceloleite.mercado.api.negotiation.util.OldHttpConnection;
import org.marceloleite.mercado.api.negotiation.util.NonceUtil;
import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.model.TapiInformation;

public abstract class OldAbstractTapiMethod<T extends TapiResponseTemplate<?, ?>> {

	private static final Logger LOGGER = LogManager.getLogger(OldAbstractTapiMethod.class);

	private static final String DOMAIN_ADDRESS = "https://www.mercadobitcoin.net";

	private static final String TAPI_PATH = "/tapi/v3/";

	private static final String PARAMETER_TAPI_METHOD = "tapi_method";

	private static final String PARAMETER_TAPI_NONCE = "tapi_nonce";

	private static final int MAXIMUM_RETRIES = 5;

	private TapiMethod tapiMethod;

	private Class<?> responseClass;

	private String[] parameterNames;

	private TapiInformation tapiInformation;

	public OldAbstractTapiMethod(TapiInformation tapiInformation, TapiMethod tapiMethod, Class<?> responseClass,
			String[] parameterNames) {
		this.tapiInformation = tapiInformation;
		this.tapiMethod = tapiMethod;
		this.responseClass = responseClass;
		this.parameterNames = parameterNames;
	}

	private NapiParameters generateNapiParameters(Object... objectParameters) {
		NapiParameters napiMethodParameters = new NapiParameters();
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

	private void sendHttpUrlConnectionProperties(HttpsURLConnection httpsUrlConnection,
			NapiParameters tapiMethodParameters) throws IOException {
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsUrlConnection.getOutputStream());
		outputStreamWriter.write(tapiMethodParameters.toUrlParametersString());
		outputStreamWriter.flush();
		outputStreamWriter.close();
	}

	private String readHttpUrlConnectionResponse(HttpsURLConnection httpsUrlConnection) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsUrlConnection.getInputStream()));
		StringBuffer stringBuffer = new StringBuffer();
		String buffer;
		while ((buffer = bufferedReader.readLine()) != null) {
			stringBuffer.append(buffer + "\n");
		}
		return stringBuffer.toString();
	}

	private NapiResponse generateJsonTapiResponse(String response) {
		return new ObjectToJsonConverter(NapiResponse.class).convertFromToObject(response, NapiResponse.class);
	}
	
	protected T executeMethod(Object... parameters) {
		NapiParameters napiParameters = generateNapiParameters(parameters);
		URI uri = generateURI(napiParameters);
		LOGGER.debug("Url generated is: " + uri);
		HttpsURLConnection httpsUrlConnection = new OldHttpConnection(tapiInformation).createHttpsUrlConnection(url);
		String response = null;
		NapiResponse jsonTapiResponse = null;
		int retries = 0;
		boolean methodExecuted = false;
		while (!methodExecuted && retries < MAXIMUM_RETRIES) {
			try {
				httpsUrlConnection.connect();
				sendHttpUrlConnectionProperties(httpsUrlConnection, napiParameters);
				response = readHttpUrlConnectionResponse(httpsUrlConnection);
				LOGGER.debug("Response received: " + response);
				methodExecuted = true;
			} catch (UnknownHostException exception) {
				LOGGER.debug("Unknown host exception: " + exception.getMessage());
				retries++;
			} catch (IOException exception) {
				throw new RuntimeException("Error while connecting to URL \"" + url + "\".", exception);
			}
		}

		if (retries >= MAXIMUM_RETRIES) {
			throw new RuntimeException("Could not connect to host \"" + url.getHost() + "\".");
		}

		jsonTapiResponse = generateJsonTapiResponse(response);
		return generateMethodResponse(jsonTapiResponse);
	}

//	protected T executeMethod(Object... parameters) {
//		NapiParameters napiParameters = generateNapiParameters(parameters);
//		URI uri = generateURI(napiParameters);
//		LOGGER.debug("Url generated is: " + uri);
//		HttpsURLConnection httpsUrlConnection = new HttpConnection(tapiInformation).createHttpsUrlConnection(url);
//		String response = null;
//		NapiResponse jsonTapiResponse = null;
//		int retries = 0;
//		boolean methodExecuted = false;
//		while (!methodExecuted && retries < MAXIMUM_RETRIES) {
//			try {
//				httpsUrlConnection.connect();
//				sendHttpUrlConnectionProperties(httpsUrlConnection, napiParameters);
//				response = readHttpUrlConnectionResponse(httpsUrlConnection);
//				LOGGER.debug("Response received: " + response);
//				methodExecuted = true;
//			} catch (UnknownHostException exception) {
//				LOGGER.debug("Unknown host exception: " + exception.getMessage());
//				retries++;
//			} catch (IOException exception) {
//				throw new RuntimeException("Error while connecting to URL \"" + url + "\".", exception);
//			}
//		}
//
//		if (retries >= MAXIMUM_RETRIES) {
//			throw new RuntimeException("Could not connect to host \"" + url.getHost() + "\".");
//		}
//
//		jsonTapiResponse = generateJsonTapiResponse(response);
//		return generateMethodResponse(jsonTapiResponse);
//	}
	
	private URI generateURI(NapiParameters napiParameters) {
		try {
			URIBuilder uriBuilder = new URIBuilder().setScheme("https")
					.setHost("www.mercadobitcoin.net")
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

	@SuppressWarnings("unchecked")
	private T generateMethodResponse(NapiResponse jsonTapiResponse) {
		try {
			Constructor<?> constructor = responseClass.getConstructor(NapiResponse.class);
			Object object = constructor.newInstance(jsonTapiResponse);
			return (T) object;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException exception) {
			throw new RuntimeException("Error while creating \"" + tapiMethod + "\" method response.\n", exception);
		}
	}
}
