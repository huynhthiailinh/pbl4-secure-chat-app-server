package com.dut.sweetchatapp.service;

import com.dut.sweetchatapp.model.PasswordResetToken;

public interface PasswordResetTokenService {

    void createPasswordResetToken(int accountId, String token);

    PasswordResetToken findByAccountId(int accountId);

    String validatePasswordResetToken(String token);

    Boolean isTokenExpired(PasswordResetToken passwordResetToken);

    int getAccountIdByToken(String token);

    void deletedToken(String token);

}
