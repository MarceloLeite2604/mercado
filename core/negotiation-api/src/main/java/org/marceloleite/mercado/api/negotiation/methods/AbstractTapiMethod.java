package org.marceloleite.mercado.api.negotiation.methods;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.api.negotiation.util.HttpConnection;
import org.marceloleite.mercado.api.negotiation.util.NonceGenerator;
import org.marceloleite.mercado.api.negotiation.util.UrlGenerator;
import org.marceloleite.mercado.base.model.TapiInformation;
import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;

public abstract class AbstractTapiMethod<T extends AbstractTapiResponse<?, ?>> {

	private static final Logger LOGGER = LogManager.getLogger(AbstractTapiMethod.class);

	private static final String DOMAIN_ADDRESS = "https://www.mercadobitcoin.net";

	private static final String TAPI_PATH = "/tapi/v3/";

	private static final String PARAMETER_TAPI_METHOD = "tapi_method";

	private static final String PARAMETER_TAPI_NONCE = "tapi_nonce";

	private TapiMethod tapiMethod;

	private Class<?> responseClass;

	private String[] parameterNames;
	
	private TapiInformation tapiInformation;
	
	public AbstractTapiMethod(TapiInformation tapiInformation, TapiMethod tapiMethod, Class<?> responseClass, String[] parameterNames) {
		this.tapiInformation = tapiInformation;
		this.tapiMethod = tapiMethod;
		this.responseClass = responseClass;
		this.parameterNames = parameterNames;
	}

	private TapiMethodParameters generateTapiMethodParameters(Object... objectParameters) {
		TapiMethodParameters tapiMethodParameters = new TapiMethodParameters();
		tapiMethodParameters.put(PARAMETER_TAPI_METHOD, tapiMethod);
		tapiMethodParameters.put(PARAMETER_TAPI_NONCE, NonceGenerator.getInstance().nextNonce());

		if (parameterNames != null && parameterNames.length > 0) {
			if (objectParameters.length != parameterNames.length) {
				throw new RuntimeException("Method \"" + tapiMethod + "\" requires " + parameterNames.length
						+ " parameter(s) to be executed, but only " + objectParameters.length + " was(were) informed.");
			}

			int counter = 0;
			for (String parameterName : parameterNames) {
				tapiMethodParameters.put(parameterName, objectParameters[counter++]);
			}
		}
		return tapiMethodParameters;
	}

	private String generateAddress() {
		return DOMAIN_ADDRESS + TAPI_PATH;
	}

	private void sendHttpUrlConnectionProperties(HttpsURLConnection httpsUrlConnection,
			TapiMethodParameters tapiMethodParameters) throws IOException {
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

	private JsonTapiResponse generateJsonTapiResponse(String response) {
		return new ObjectToJsonConverter(JsonTapiResponse.class).convertFromToObject(response, JsonTapiResponse.class);
	}

	protected T executeMethod(Object... objectParameters) {
		TapiMethodParameters tapiMethodParameters = generateTapiMethodParameters(objectParameters);
		URL url = new UrlGenerator().generate(generateAddress(), tapiMethodParameters);
		LOGGER.debug("Url generated is: " + url);
		HttpsURLConnection httpsUrlConnection = new HttpConnection(tapiInformation).createHttpsUrlConnection(url);
		try {
			httpsUrlConnection.connect();
			sendHttpUrlConnectionProperties(httpsUrlConnection, tapiMethodParameters);
			String response = readHttpUrlConnectionResponse(httpsUrlConnection);
			LOGGER.debug("Response received: " + response);
			JsonTapiResponse jsonTapiResponse = generateJsonTapiResponse(response);
			return generateMethodResponse(jsonTapiResponse);
		} catch (IOException exception) {
			throw new RuntimeException("Error while connecting to URL \"" + url + "\".", exception);
		}
	}

	@SuppressWarnings("unchecked")
	private T generateMethodResponse(JsonTapiResponse jsonTapiResponse) {
		try {
			Constructor<?> constructor = responseClass.getConstructor(JsonTapiResponse.class);
			Object object = constructor.newInstance(jsonTapiResponse);
			return (T) object;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException exception) {
			throw new RuntimeException("Error while creating \"" + tapiMethod + "\" method response.\n", exception);
		}
	}
}
