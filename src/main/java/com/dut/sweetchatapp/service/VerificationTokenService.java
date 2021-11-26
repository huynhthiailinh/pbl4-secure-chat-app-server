package com.dut.sweetchatapp.service;

import com.dut.sweetchatapp.model.VerificationToken;

import java.sql.Timestamp;

public interface VerificationTokenService {

    VerificationToken findByToken(String token);

    VerificationToken findByAccountId(int accountId);

    void save(int accountId);

    Timestamp calculateExpiryDate(int expiryTimeInMinutes);

    boolean existsByAccountId(int accountId);

}
