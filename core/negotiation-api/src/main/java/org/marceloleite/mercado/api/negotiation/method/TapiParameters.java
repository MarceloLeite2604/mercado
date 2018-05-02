package org.marceloleite.mercado.api.negotiation.method;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;

public class TapiParameters extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	public String toUrlParametersString() throws UnsupportedEncodingException {
		String result = "";
		for (String propertyKey : keySet()) {
			Object propertyValue = get(propertyKey);
			if (propertyValue != null) {
				result += propertyKey + "=" + URLEncoder.encode(propertyValue.toString(), StandardCharsets.UTF_8.name())
						+ "&";
			}
		}

		result = result.substring(0, result.length() - 1);
		return result;
	}
}
