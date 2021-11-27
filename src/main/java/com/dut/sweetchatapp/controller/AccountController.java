package com.dut.sweetchatapp.controller;

import com.dut.sweetchatapp.dto.MessageResponse;
import com.dut.sweetchatapp.model.Account;
import com.dut.sweetchatapp.service.AccountService;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.dut.sweetchatapp.constant.DefaultPath.ACCOUNT_PATH;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(ACCOUNT_PATH)
public class AccountController {

    private final AccountService accountService;

    @GetMapping("summaries/{accountId}")
    public ResponseEntity<?> findAllUserSummaries(@PathVariable int accountId) {
        return ResponseEntity.ok(accountService
                .getAllAccounts()
                .stream()
                .filter(account -> account.getId() != accountId));
    }

    @PutMapping
    public ResponseEntity updateProfile(@RequestParam String profileString, @RequestParam(required = false) MultipartFile multipartFile) {
        Gson gson = new Gson();
        Account account = gson.fromJson(profileString, Account.class);
        accountService.updateProfile(account, multipartFile);
        return new ResponseEntity<>(new MessageResponse("Update profile success!"), HttpStatus.OK);
    }

}
