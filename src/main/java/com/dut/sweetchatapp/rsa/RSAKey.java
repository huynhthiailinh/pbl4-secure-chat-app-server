package com.dut.sweetchatapp.rsa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.security.PrivateKey;
import java.security.PublicKey;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RSAKey {

    private PublicKey publicKey;

    private PrivateKey privateKey;

}
