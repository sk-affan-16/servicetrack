package com.servicetrack.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

public final class PasswordUtil {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    private static final int ITERATIONS = 600_000;

    private static final int SALT_LENGTH = 16;

    private static final int KEY_LENGTH = 256;

    private PasswordUtil() {
        // Prevent object creation
    }

    public static String hashPassword(String password) {

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        byte[] salt = new byte[SALT_LENGTH];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);

        byte[] hash = deriveKey(password.toCharArray(), salt);

        return "pbkdf2_sha256$"
                + ITERATIONS
                + "$"
                + Base64.getEncoder().encodeToString(salt)
                + "$"
                + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyPassword(String password, String storedHash) {

        if (password == null || storedHash == null || storedHash.isBlank()) {
            return false;
        }

        try {
            String[] parts = storedHash.split("\\$");

            if (parts.length != 4) {
                return false;
            }

            if (!"pbkdf2_sha256".equals(parts[0])) {
                return false;
            }

            int iterations = Integer.parseInt(parts[1]);

            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[3]);

            byte[] actualHash = deriveKey(
                    password.toCharArray(),
                    salt,
                    iterations,
                    expectedHash.length * 8
            );

            return constantTimeEquals(actualHash, expectedHash);

        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] deriveKey(char[] password, byte[] salt) {
        return deriveKey(password, salt, ITERATIONS, KEY_LENGTH);
    }

    private static byte[] deriveKey(
            char[] password,
            byte[] salt,
            int iterations,
            int keyLength) {

        try {
            PBEKeySpec spec = new PBEKeySpec(
                    password,
                    salt,
                    iterations,
                    keyLength
            );

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(ALGORITHM);

            byte[] hash = factory.generateSecret(spec).getEncoded();

            spec.clearPassword();

            return hash;

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Unable to hash password",
                    e
            );
        }
    }

    private static boolean constantTimeEquals(
            byte[] first,
            byte[] second) {

        if (first.length != second.length) {
            return false;
        }

        int result = 0;

        for (int i = 0; i < first.length; i++) {
            result |= first[i] ^ second[i];
        }

        return result == 0;
    }
}