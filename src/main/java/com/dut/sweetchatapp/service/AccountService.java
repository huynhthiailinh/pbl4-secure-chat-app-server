package com.dut.sweetchatapp.service;

import com.dut.sweetchatapp.model.Account;

import java.util.List;

public interface AccountService {

    Boolean checkIfEnabled(String name);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Account save(Account account);

    List<Account> getAllAccounts();

    Account getAccountById(int id);

    Account getAccountByEmail(String email);

    byte[] getPublicKeyByAccountId(int accountId);

    byte[] getPrivateKeyByAccountId(int accountId);

    void updateAvatarById(int id, String avatar);

    void updateFullNameById(int accountId, String fullName);

    void updatePasswordByAccountId(int accountId, String password);

}
