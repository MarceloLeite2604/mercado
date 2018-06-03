package org.marceloleite.mercado.api.negotiation.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class EncryptionUtil {

	private static final String ENCRYPTION_ALGORITHM = "HmacSHA512";

	private EncryptionUtil() {
	}

	public static String generateTapiMac(URI uri, String requestBody, byte[] key) {
		try {
			Mac mac = Mac.getInstance(ENCRYPTION_ALGORITHM);
			SecretKeySpec secretKeySpec = new SecretKeySpec(key, ENCRYPTION_ALGORITHM);
			mac.init(secretKeySpec);
			String contentToEncrypt = uri.getPath() + "?" + requestBody;
			// String contentToEncrypt = uri.toString();

			byte[] macData = mac.doFinal(contentToEncrypt.getBytes(StandardCharsets.UTF_8.name()));
			return byteToStringHex(macData);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException exception) {
			throw new RuntimeException(exception);
		}
	}

	private static String byteToStringHex(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		for (byte bByte : bytes) {
			stringBuffer.append(String.format("%02X", bByte));
		}
		return stringBuffer.toString()
				.toLowerCase();
	}
}
