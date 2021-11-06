package com.dut.sweetchatapp.rsa;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.PublicKey;
import java.security.PrivateKey;

public class SecurityKeyPairGenerator {

    public static RSAKey generateKeyPair() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024, secureRandom);

        // Initialize key pair
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        // Public Key
        PublicKey publicKey = keyPair.getPublic();
        // Private Key
        PrivateKey privateKey = keyPair.getPrivate();

        return new RSAKey(publicKey, privateKey);
    }

}
