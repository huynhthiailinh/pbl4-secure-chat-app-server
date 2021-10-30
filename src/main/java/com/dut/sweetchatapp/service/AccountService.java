package com.dut.sweetchatapp.service;

import com.dut.sweetchatapp.model.Account;
import java.util.List;

public interface AccountService {

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Account save(Account account);

    List<Account> getAllAccounts();

}
