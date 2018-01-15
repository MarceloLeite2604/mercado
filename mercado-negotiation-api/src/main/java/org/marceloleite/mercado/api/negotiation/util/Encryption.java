package org.marceloleite.mercado.api.negotiation.util;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

	private static final String ENCRYPTION_ALGORITHM = "HmacSHA512";

	public String generateTapiMac(URL url, byte[] key) {
		try {
			Mac mac = Mac.getInstance(ENCRYPTION_ALGORITHM);
			SecretKeySpec secretKeySpec = new SecretKeySpec(key, ENCRYPTION_ALGORITHM);
			mac.init(secretKeySpec);
			String contentToEncrypt = url.getPath() + "?" + url.getQuery();

			byte[] macData = mac.doFinal(contentToEncrypt.getBytes(StandardCharsets.UTF_8.name()));
			return byteToStringHex(macData);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException exception) {
			throw new RuntimeException(exception);
		}
	}

	private String byteToStringHex(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		for (byte bByte : bytes) {
			stringBuffer.append(String.format("%02X", bByte));
		}
		return stringBuffer.toString().toLowerCase();
	}
}
