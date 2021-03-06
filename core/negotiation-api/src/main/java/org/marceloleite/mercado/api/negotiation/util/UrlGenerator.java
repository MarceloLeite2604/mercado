package org.marceloleite.mercado.api.negotiation.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.marceloleite.mercado.api.negotiation.methods.TapiMethodParameters;

public class UrlGenerator {

	public URL generate(String generateAddress, TapiMethodParameters tapiMethodParameters) {
		try {
			String queryString = "?";
			if (tapiMethodParameters != null && !tapiMethodParameters.isEmpty()) {
				for (String parameterKey : tapiMethodParameters.keySet()) {
					Object parameterValue = tapiMethodParameters.get(parameterKey);
					if (parameterValue != null) {
						queryString += parameterKey + "="
								+ URLEncoder.encode(parameterValue.toString(), StandardCharsets.UTF_8.name()) + "&";
					}
				}
				queryString = queryString.substring(0, queryString.length() - 1);
			}
			return new URL(generateAddress + queryString);
		} catch (UnsupportedEncodingException | MalformedURLException exception) {
			throw new RuntimeException(exception);
		}
	}

}
