package com.dut.sweetchatapp.controller;

import com.dut.sweetchatapp.dto.MessageResponse;
import com.dut.sweetchatapp.enums.EmailVerification;
import com.dut.sweetchatapp.model.Account;
import com.dut.sweetchatapp.model.VerificationToken;
import com.dut.sweetchatapp.service.AccountService;
import com.dut.sweetchatapp.service.VerificationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

import static com.dut.sweetchatapp.constant.DefaultPath.ACTIVATION_PATH;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class VerificationController {

    private final VerificationTokenService verificationTokenService;

    private final AccountService accountService;

    @GetMapping(ACTIVATION_PATH)
    public ResponseEntity activation(@RequestParam("token") String token) {

        EmailVerification emailVerification;

        VerificationToken verificationToken = verificationTokenService.findByToken(token);
        if(verificationToken == null) {
            emailVerification = EmailVerification.INVALID;
        } else {
            Account account = accountService.getAccountById(verificationToken.getAccountId());

            //if the account is not activated
            if(!account.isEnabled()) {

                //get the current timestamp
                Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

                //check if the token is expired
                if(verificationToken.getExpiryDate().before(currentTimestamp)) {
                    emailVerification = EmailVerification.EXPIRED;
                } else {

                    //the token is valid
                    //activate the account
                    account.setEnabled(true);

                    //update the account
                    accountService.save(account);
                    emailVerification = EmailVerification.SUCCESSFULLY_ACTIVATED;
                }
            } else {

                //the account is already activated
                emailVerification = EmailVerification.ALREADY_ACTIVATED;
            }
        }
        return new ResponseEntity<>(new MessageResponse(emailVerification.toString()), HttpStatus.OK);
    }

}
