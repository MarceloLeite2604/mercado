package org.marceloleite.mercado.api.negotiation.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

	private static Encryption instance;

	private static final String ENCRYPTION_ALGORITHM = "HmacSHA512";

	private Encryption() {
	}

	public String generateTapiMac(URI uri, byte[] key) {
		try {
			Mac mac = Mac.getInstance(ENCRYPTION_ALGORITHM);
			SecretKeySpec secretKeySpec = new SecretKeySpec(key, ENCRYPTION_ALGORITHM);
			mac.init(secretKeySpec);
			// String contentToEncrypt = uri.getPath() + "?" + uri.getQuery();
			String contentToEncrypt = uri.toString();

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
		return stringBuffer.toString()
				.toLowerCase();
	}

	public static Encryption getInstance() {
		if (instance == null) {
			instance = new Encryption();
		}
		return instance;
	}
}
