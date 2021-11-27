package com.dut.sweetchatapp.service.impl;

import com.dut.sweetchatapp.constant.DefaultParam;
import com.dut.sweetchatapp.handleException.exception.ObjectNotFoundException;
import com.dut.sweetchatapp.model.Account;
import com.dut.sweetchatapp.repository.AccountRepository;
import com.dut.sweetchatapp.service.AccountService;
import com.dut.sweetchatapp.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final ImageService imageService;

    @Override
    public Boolean checkIfEnabled(String name) {
        return accountRepository.findByUsernameOrEmail(name, name).get().isEnabled();
    }

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
    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public Account getAccountById(int id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Account with id " + id + " not found"));
    }

    @Override
    public byte[] getPublicKeyByAccountId(int accountId) {
        return accountRepository.getPublicKeyByAccountId(accountId);
    }

    @Override
    public byte[] getPrivateKeyByAccountId(int accountId) {
        return accountRepository.getPrivateKeyByAccountId(accountId);
    }

    @Override
    public void updateProfile(Account account, MultipartFile multipartFile) {
        if (!Objects.isNull(multipartFile) && !Objects.requireNonNull(multipartFile.getOriginalFilename()).isEmpty()) {
            account.setAvatar(imageService.uploadToLocalFileSystem(multipartFile,
                    DefaultParam.AVATAR, account.getId()));
        } else {
            account.setAvatar(accountRepository.getAvatarByAccountId(account.getId()));
        }
        accountRepository.updateProfile(account.getFullName(), account.getEmail(),
                account.getGender(), account.getAvatar(), account.getId());
    }

}
