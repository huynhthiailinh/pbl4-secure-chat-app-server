package com.dut.sweetchatapp.controller;

import com.dut.sweetchatapp.service.AccountService;
import com.dut.sweetchatapp.service.ImageService;
import lombok.AllArgsConstructor;
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

    private final ImageService imageService;

    @GetMapping("summaries/{accountId}")
    public ResponseEntity<?> findAllUserSummaries(@PathVariable int accountId) {
        return ResponseEntity.ok(accountService
                .getAllAccounts()
                .stream()
                .filter(account -> account.getId() != accountId));
    }

    @PostMapping("upload-avatar")
    public String uploadAvatar(@RequestParam String type, @RequestParam int id, @RequestParam MultipartFile multipartFile) {
        String imageUrl = "";
        accountService.updateAvatarById(id, imageService.uploadToLocalFileSystem(multipartFile, type, id));
        imageUrl = accountService.getAccountById(id).getAvatar();
        return imageUrl;
    }

}
