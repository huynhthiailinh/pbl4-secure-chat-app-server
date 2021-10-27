package com.dut.sweetchatapp.service.impl;

import com.dut.sweetchatapp.model.Account;
import com.dut.sweetchatapp.repository.AccountRepository;
import com.dut.sweetchatapp.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account addAccount(Account account) {
        return accountRepository.save(account);
    }

}
