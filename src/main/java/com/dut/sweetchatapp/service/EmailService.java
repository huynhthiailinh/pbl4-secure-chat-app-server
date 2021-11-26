package com.dut.sweetchatapp.service;

import com.dut.sweetchatapp.model.Account;

import javax.mail.MessagingException;

public interface EmailService {

    void sendVerificationEmail(Account account) throws MessagingException;

}
