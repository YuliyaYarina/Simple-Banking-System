package org.example.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import static java.util.Base64.*;

public class PinHasher {
    private static final int ITERATIONS = 65_536;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PinHasher() {
    }

    public static String hash(String pin) {
        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);
        byte[] hash = pbkdf2(pin.toCharArray(), salt);
        return getEncoder().encodeToString(salt) + ":" + getEncoder().encodeToString(hash);
    }

    public static boolean verify(String pin, String storedValue) {
        String[] parts = storedValue.split(":");
        if (parts.length != 2) {
            return false;
        }
        byte[] salt = getDecoder().decode(parts[0]);
        byte[] expectedHash = getDecoder().decode(parts[1]);
        byte[] candidateHash = pbkdf2(pin.toCharArray(), salt);
        if (expectedHash.length != candidateHash.length) {
            return false;
        }
        int diff = 0;
        for (int i = 0; i < expectedHash.length; i++) {
            diff |= expectedHash[i] ^ candidateHash[i];
        }
        return diff == 0;
    }

    private static byte[] pbkdf2(char[] value, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(value, salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("PIN hashing failed", e);
        }
    }
}
