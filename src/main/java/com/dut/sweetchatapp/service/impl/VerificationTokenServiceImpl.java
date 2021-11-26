package com.dut.sweetchatapp.service.impl;

import com.dut.sweetchatapp.model.VerificationToken;
import com.dut.sweetchatapp.repository.VerificationTokenRepository;
import com.dut.sweetchatapp.service.VerificationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

@AllArgsConstructor
@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public VerificationToken findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public VerificationToken findByAccountId(int accountId) {
        return verificationTokenRepository.findByAccountId(accountId);
    }

    @Override
    public void save(int accountId) {

        VerificationToken verificationToken = existsByAccountId(accountId) ?
                findByAccountId(accountId)
                : VerificationToken.builder().accountId(accountId).build();

        String token = UUID.randomUUID().toString();
        verificationToken.setToken(token);

        //set expiry date to 24 hours
        verificationToken.setExpiryDate(calculateExpiryDate(24*60));

        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public Timestamp calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Timestamp(calendar.getTime().getTime());
    }

    @Override
    public boolean existsByAccountId(int accountId) {
        return verificationTokenRepository.existsByAccountId(accountId);
    }

}
