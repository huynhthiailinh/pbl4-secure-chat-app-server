package com.dut.sweetchatapp.service.impl;

import com.dut.sweetchatapp.model.PasswordResetToken;
import com.dut.sweetchatapp.repository.PasswordResetTokenRepository;
import com.dut.sweetchatapp.service.PasswordResetTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.dut.sweetchatapp.enums.ForgotPasswordResponse.ENTER_NEW_PASSWORD;
import static com.dut.sweetchatapp.enums.ForgotPasswordResponse.EXPIRED_TOKEN;
import static com.dut.sweetchatapp.enums.ForgotPasswordResponse.INVALID_TOKEN;

@AllArgsConstructor
@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public void createPasswordResetToken(int accountId, String token) {
        if(passwordResetTokenRepository.existsByAccountId(accountId)) {
            PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByAccountId(accountId);
            passwordResetToken.setToken(token);
            passwordResetToken.setExpiryDate(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30)));
            passwordResetTokenRepository.save(passwordResetToken);
        } else {
            PasswordResetToken passwordResetToken = new PasswordResetToken(token, accountId);
            passwordResetTokenRepository.save(passwordResetToken);
        }
    }

    @Override
    public PasswordResetToken findByAccountId(int accountId) {
        return passwordResetTokenRepository.findByAccountId(accountId);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

        return !passwordResetTokenRepository.existsByToken(token) ? INVALID_TOKEN.toString()
                : isTokenExpired(passwordResetToken) ? EXPIRED_TOKEN.toString()
                : ENTER_NEW_PASSWORD.toString();
    }

    @Override
    public Boolean isTokenExpired(PasswordResetToken passwordResetToken) {
        final Calendar calendar = Calendar.getInstance();
        return passwordResetToken.getExpiryDate().before(calendar.getTime());
    }

    @Override
    public int getAccountIdByToken(String token) {
        return passwordResetTokenRepository.findByToken(token).getAccountId();
    }

    @Override
    public void deletedToken(String token) {
        passwordResetTokenRepository.deleteByToken(token);
    }

}
