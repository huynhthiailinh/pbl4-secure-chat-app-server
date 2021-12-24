package com.dut.sweetchatapp.controller;

import com.dut.sweetchatapp.dto.ChangePasswordDTO;
import com.dut.sweetchatapp.dto.MessageResponse;
import com.dut.sweetchatapp.model.Account;
import com.dut.sweetchatapp.service.AccountService;
import com.dut.sweetchatapp.service.EmailService;
import com.dut.sweetchatapp.service.PasswordResetTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.Objects;
import java.util.UUID;

import static com.dut.sweetchatapp.constant.DefaultPath.FORGOT_PASSWORD_PATH;
import static com.dut.sweetchatapp.constant.DefaultPath.RESET_PASSWORD_PATH;
import static com.dut.sweetchatapp.constant.DefaultPath.SAVE_PASSWORD_PATH;
import static com.dut.sweetchatapp.enums.ForgotPasswordResponse.CHECK_EMAIL_TO_RESET_PASSWORD;
import static com.dut.sweetchatapp.enums.ForgotPasswordResponse.ENTER_NEW_PASSWORD;
import static com.dut.sweetchatapp.enums.ForgotPasswordResponse.RETURN_HOME_PAGE;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ForgotPasswordController {

    private final AccountService accountService;

    private final PasswordResetTokenService passwordResetTokenService;

    private final EmailService emailService;

    @PostMapping(FORGOT_PASSWORD_PATH)
    public ResponseEntity forgotPassword(@RequestParam("email") String email) throws AccountNotFoundException, MessagingException {
        Account account = accountService.getAccountByEmail(email);

        if(account == null) {
            throw new AccountNotFoundException();
        }

        String token = UUID.randomUUID().toString();
        passwordResetTokenService.createPasswordResetToken(account.getId(), token);
        emailService.sendResetPasswordEmail(email);
        return new ResponseEntity<>(
                new MessageResponse(CHECK_EMAIL_TO_RESET_PASSWORD.toString()),
                HttpStatus.OK
        );
    }

    @GetMapping(RESET_PASSWORD_PATH)
    public ResponseEntity resetPassword(@RequestParam("token") String token) {
        String result = passwordResetTokenService.validatePasswordResetToken(token);
        return new ResponseEntity<>(new MessageResponse(result), HttpStatus.OK);
    }

    @PutMapping(SAVE_PASSWORD_PATH)
    public ResponseEntity savePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        String result =
                passwordResetTokenService.validatePasswordResetToken(changePasswordDTO.getToken());

        if (Objects.equals(result, ENTER_NEW_PASSWORD.toString())) {
            result = RETURN_HOME_PAGE.toString();
        } else {
            return new ResponseEntity<>(new MessageResponse(result), HttpStatus.OK);
        }

        Account account = accountService.getAccountById(
                passwordResetTokenService.getAccountIdByToken(
                        changePasswordDTO.getToken()
                )
        );

        accountService.updatePasswordByAccountId(account.getId(), changePasswordDTO.getPassword());
        passwordResetTokenService.deletedToken(changePasswordDTO.getToken());
        return new ResponseEntity<>(new MessageResponse(result), HttpStatus.OK);
    }

}
