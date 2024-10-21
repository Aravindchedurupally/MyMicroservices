package com.sample.dental.smile.dentail.work.flow.password.dec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class PasswordEncryptDecrypt {

	private static final String ALGORITHM = "AES";
	private static final String SECRET_KEY = "MySuperSecretKey"; // Use a strong key in production

	// Method to encrypt the password
	public static String encrypt(String password) throws Exception {
		SecretKeySpec keySpec = generateKey(SECRET_KEY);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		byte[] encryptedBytes = cipher.doFinal(password.getBytes());
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	// Method to decrypt the password
	public static String decrypt(String encryptedPassword) throws Exception {
		SecretKeySpec keySpec = generateKey(SECRET_KEY);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, keySpec);
		byte[] decodedBytes = Base64.getDecoder().decode(encryptedPassword);
		byte[] decryptedBytes = cipher.doFinal(decodedBytes);
		return new String(decryptedBytes);
	}

	// Utility method to generate a SecretKeySpec from a given key string
	private static SecretKeySpec generateKey(String key) throws Exception {
		byte[] keyBytes = key.getBytes("UTF-8");
		return new SecretKeySpec(keyBytes, ALGORITHM);
	}
}
