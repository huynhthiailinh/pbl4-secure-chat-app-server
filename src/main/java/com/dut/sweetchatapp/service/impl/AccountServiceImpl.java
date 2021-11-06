package com.dut.sweetchatapp.service.impl;

import com.dut.sweetchatapp.model.Account;
import com.dut.sweetchatapp.repository.AccountRepository;
import com.dut.sweetchatapp.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        return (List<Account>) accountRepository.findAll();
    }

    @Override
    public byte[] getPublicKeyByAccountId(int accountId) {
        return accountRepository.getPublicKeyByAccountId(accountId);
    }

    @Override
    public byte[] getPrivateKeyByAccountId(int accountId) {
        return accountRepository.getPrivateKeyByAccountId(accountId);
    }

}
