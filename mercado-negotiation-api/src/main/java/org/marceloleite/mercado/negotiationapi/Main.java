package org.marceloleite.mercado.negotiationapi;

import org.marceloleite.mercado.commons.util.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;
import org.marceloleite.mercado.negotiationapi.listorders.ListOrdersMethod;
import org.marceloleite.mercado.negotiationapi.listorders.ListOrdersMethodResponse;
import org.marceloleite.mercado.negotiationapi.model.CurrencyPair;

public class Main {
	public static void main(String[] args) throws Exception {

		listOrdersMethod();
		// Long tapiNonce = 6l;

		// Elaborate URL.
		/*TapiMethodParameters properties = new TapiMethodParameters();
		properties.put(TAPI_METHOD, TapiMethod.LIST_ORDER);
		properties.put(TAPI_NONCE, tapiNonce);
		properties.put(PARAMETER_COIN_PAIR, "" + Currency.REAL + Currency.BITCOIN);
		String stringParameters = properties.toUrlParametersString();
		URL url = new URL(DOMAIN_ADDRESS + TAPI_PATH + "?" + stringParameters);
		System.out.println("URL request is: " + url + ".");*/

		// Elaborate HTTP request.
		/*Map<String, Object> httpRequestProperties = new HashMap<>();
		httpRequestProperties.put("Content-Type", "application/x-www-form-urlencoded");
		String tapiMac = encrypt(url.getPath() + "?" + url.getQuery(), System.getenv(VARIABLE_SECRET));
		httpRequestProperties.put(TAPI_ID, System.getenv(VARIABLE_ID));
		httpRequestProperties.put(TAPI_MAC, tapiMac);
		HttpsURLConnection httpsUrlConnection = createHttpsRequestConnection(url, HTTP_POST_METHOD,
				httpRequestProperties, true);*/

		// Send request.
		/*httpsUrlConnection.connect();
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsUrlConnection.getOutputStream());
		outputStreamWriter.write(stringParameters);
		outputStreamWriter.flush();
		outputStreamWriter.close();*/

		// Read response.
		/*BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsUrlConnection.getInputStream()));
		StringBuffer stringBuffer = new StringBuffer();
		String buffer;
		while ((buffer = bufferedReader.readLine()) != null) {
			stringBuffer.append(buffer + "\n");
		}
		System.out.println(stringBuffer.toString());*/
	}

	private static void listOrdersMethod() {
		try {
			ListOrdersMethodResponse listOrdersMethodResponse = new ListOrdersMethod().execute(CurrencyPair.BRLBCH);
			System.out.println(new ObjectToJsonConverter().convertTo(listOrdersMethodResponse));
		} finally {
			EntityManagerController.getInstance().close();
		}
		
		
	}

	/*private static HttpsURLConnection createHttpsRequestConnection(URL url, String httpMethod,
			Map<String, Object> properties, boolean doOutput) throws IOException {
		HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) url.openConnection();

		httpsUrlConnection.setRequestMethod(HTTP_POST_METHOD);

		if (properties != null && !properties.isEmpty()) {
			for (String propertyKey : properties.keySet()) {
				Object property = properties.get(propertyKey);
				httpsUrlConnection.setRequestProperty(propertyKey, property.toString());
			}
		}

		// Send a body with the request.
		httpsUrlConnection.setDoOutput(doOutput);

		return httpsUrlConnection;
	}*/

	/*private static String generateUrl(String url, Map<String, Object> properties) throws UnsupportedEncodingException {
		String result = url + "?";
		for (String propertyKey : properties.keySet()) {
			Object propertyValue = properties.get(propertyKey);
			result += propertyKey + "=" + URLEncoder.encode(propertyValue.toString(), StandardCharsets.UTF_8.name())
					+ "&";
		}

		result = result.substring(0, result.length() - 1);
		return result;
	}*/

	/*private static String encrypt(String content, String key)
			throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, UnsupportedEncodingException {
		Mac mac = Mac.getInstance(ENCRYPTION_ALGORITHM);
		SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8.name()),
				ENCRYPTION_ALGORITHM);
		mac.init(secretKeySpec);
		byte[] macData = mac.doFinal(content.getBytes(StandardCharsets.UTF_8.name()));
		StringBuffer stringBuffer = new StringBuffer();
		for (byte bByte : macData) {
			stringBuffer.append(String.format("%02X", bByte));
		}
		return stringBuffer.toString().toLowerCase();
	}*/
}
