package com.dut.sweetchatapp.service;

import com.dut.sweetchatapp.model.Account;

public interface AccountService {

    Account addAccount(Account account);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Account save(Account account);

}
