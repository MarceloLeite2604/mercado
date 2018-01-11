package org.marceloleite.mercado.negotiationapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

public class Main {

	private static final String URL_PATH = "https://www.mercadobitcoin.net/tapi/v3/";

	private static final String TAPI_ID = "TAPI-ID";

	private static final String TAPI_MAC = "TAPI-MAC";

	private static final String TAPI_METHOD = "tapi_method";

	private static final String TAPI_NONCE = "tapi_nonce";

	private static final String VARIABLE_SECRET = "MBS";

	private static final String ENCRYPTION_ALGORITHM = "HmacSHA512";

	private static final String HTTP_POST_METHOD = "POST";

	public static void main(String[] args) throws Exception {

		long tapiNonce = 1l;
		String stringTapiNonce = Long.toString(tapiNonce);

		TapiMethodParameters properties = new TapiMethodParameters();
		properties.put(TAPI_METHOD, TapiMethod.LIST_ORDER);
		properties.put(TAPI_NONCE, stringTapiNonce);
		String stringUrl = generateUrl(URL_PATH, properties);
		System.out.println(System.getenv("MERCADOBITCOIN_SECRET"));
		String mercadoBitcoinSecret = System.getenv(VARIABLE_SECRET);
		String tapiMac = encrypt(stringUrl, mercadoBitcoinSecret);
		URL url = new URL(URL_PATH);

		Map<String, Object> httpRequestProperties = new HashMap<>();
		httpRequestProperties.put("Content-Type", "application/x-www-form-urlencoded");
		httpRequestProperties.put(TAPI_ID, mercadoBitcoinSecret);
		httpRequestProperties.put(TAPI_MAC, tapiMac);
		HttpsURLConnection httpsUrlConnection = createHttpsRequestConnection(url, HTTP_POST_METHOD,
				httpRequestProperties, true);

		httpsUrlConnection.connect();
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsUrlConnection.getOutputStream());
		outputStreamWriter.write(properties.toUrlParametersString());
		outputStreamWriter.flush();
		outputStreamWriter.close();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsUrlConnection.getInputStream()));
		StringBuffer stringBuffer = new StringBuffer();
		String buffer;
		while ((buffer = bufferedReader.readLine()) != null) {
			stringBuffer.append(buffer + "\n");
		}
		
		System.out.println(stringBuffer.toString());
	}

	private static HttpsURLConnection createHttpsRequestConnection(URL url, String httpMethod,
			Map<String, Object> properties, boolean doOutput) throws IOException {
		HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) url.openConnection();

		httpsUrlConnection.setRequestMethod(HTTP_POST_METHOD);

		for (String propertyKey : properties.keySet()) {
			Object property = properties.get(propertyKey);
			httpsUrlConnection.setRequestProperty(propertyKey, property.toString());
		}

		// Send a body with the request.
		httpsUrlConnection.setDoOutput(doOutput);

		return httpsUrlConnection;
	}

	private static String generateUrl(String url, Map<String, Object> properties) throws UnsupportedEncodingException {
		String result = url + "?";
		for (String propertyKey : properties.keySet()) {
			Object propertyValue = properties.get(propertyKey);
			result += propertyKey + "=" + URLEncoder.encode(propertyValue.toString(), StandardCharsets.UTF_8.name())
					+ "&";
		}

		result = result.substring(0, result.length() - 1);
		return result;
	}

	private static String encrypt(String content, String key)
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
	}
}
