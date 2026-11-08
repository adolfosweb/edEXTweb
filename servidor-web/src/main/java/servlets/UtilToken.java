package com.mycompany.edextweb;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class UtilToken {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generarToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    public static String calcularHash(String token) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(token.getBytes(StandardCharsets.UTF_8));

            StringBuilder resultado = new StringBuilder();

            for (byte b : hash) {
                resultado.append(String.format("%02x", b & 0xff));
            }

            return resultado.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No se pudo calcular el hash", e);
        }
    }
}