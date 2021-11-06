package com.dut.sweetchatapp.controller;

import com.dut.sweetchatapp.service.AccountService;
import com.dut.sweetchatapp.service.impl.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

}
