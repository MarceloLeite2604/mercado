package org.marceloleite.mercado.commons.encryption;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class Encrypt {

	public static final String KEY_ENVIRONMENT_VARIABLE_NAME = "MERCADO_ENCRYPT_KEY";

	private static final String CRYPTOGRAPHIC_ALGORITHM = "DESede";

	private static final String FEEDBACK_MODE = "CBC";

	private static final String PADDING_SCHEME = "PKCS5Padding";

	private static final String TRANSFORMATION = CRYPTOGRAPHIC_ALGORITHM + "/" + FEEDBACK_MODE + "/" + PADDING_SCHEME;

	public String encrypt(String content) {
		return encrypt(content, retrieveKey());
	}

	public String encrypt(String content, String key) {
		try {
			byte[] keyBytes = DatatypeConverter.parseBase64Binary(key);
			byte[] cryptedBytes = encryptDecrypt(content.getBytes(), keyBytes, Cipher.ENCRYPT_MODE);
			return DatatypeConverter.printBase64Binary(cryptedBytes);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | InvalidAlgorithmParameterException exception) {
			throw new RuntimeException("Error while encrypting content.", exception);
		}
	}

	public String decrypt(String content) {
		return decrypt(content, retrieveKey());
	}

	public String decrypt(String content, String key) {
		try {
			byte[] encryptedBytes = DatatypeConverter.parseBase64Binary(content);
			byte[] keyBytes = DatatypeConverter.parseBase64Binary(key);
			byte[] decryptedBytes = encryptDecrypt(encryptedBytes, keyBytes, Cipher.DECRYPT_MODE);
			return new String(decryptedBytes);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | InvalidAlgorithmParameterException exception) {
			throw new RuntimeException("Error while decrypting content.", exception);
		}
	}

	private byte[] encryptDecrypt(byte[] content, byte[] key, int opMode)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, InvalidAlgorithmParameterException {
		SecretKey secretKey = new SecretKeySpec(key, CRYPTOGRAPHIC_ALGORITHM);
		Cipher cipher = createCipher(secretKey, opMode);
		return cipher.doFinal(content);
	}

	private Cipher createCipher(final SecretKey secretKey, int opMode) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(opMode, secretKey, new IvParameterSpec(new byte[8]));
		return cipher;
	}

	private String retrieveKey() {
		String key = System.getenv(KEY_ENVIRONMENT_VARIABLE_NAME);
		if (key == null) {
			throw new IllegalStateException(
					"Could not find encrypt key environment variable \"" + KEY_ENVIRONMENT_VARIABLE_NAME + "\".");
		}

		if (key.isEmpty()) {
			throw new IllegalStateException("The encrypt key is empty.");
		}

		// return DatatypeConverter.parseBase64Binary(key);
		return key;
	}

	public String generateKey() {
		try {
			KeyGenerator keygen = KeyGenerator.getInstance(CRYPTOGRAPHIC_ALGORITHM);
			return DatatypeConverter.printBase64Binary(keygen.generateKey().getEncoded());
		} catch (NoSuchAlgorithmException exception) {
			throw new RuntimeException("Error while retrieving encryption algorythm.", exception);
		}
	}
}
