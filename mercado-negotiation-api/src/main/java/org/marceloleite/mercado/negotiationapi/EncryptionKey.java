package org.marceloleite.mercado.negotiationapi;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class EncryptionKey {

	private static final String VARIABLE_SECRET = "MBS";

	private static final String VARIABLE_ID = "MBI";

	public String retrieveKeyId() {
		return System.getenv(VARIABLE_ID);
	}

	public byte[] retrieveKeySecret() throws UnsupportedEncodingException {
		return System.getenv(VARIABLE_SECRET).getBytes(StandardCharsets.UTF_8.name());
	}
}
