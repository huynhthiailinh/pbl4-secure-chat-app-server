package com.dut.sweetchatapp.service.impl;

import com.dut.sweetchatapp.model.Account;
import com.dut.sweetchatapp.model.PasswordResetToken;
import com.dut.sweetchatapp.model.VerificationToken;
import com.dut.sweetchatapp.service.AccountService;
import com.dut.sweetchatapp.service.EmailService;
import com.dut.sweetchatapp.service.PasswordResetTokenService;
import com.dut.sweetchatapp.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${app.host}")
    private String appHost;

    private final VerificationTokenService verificationTokenService;

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    private final AccountService accountService;

    private final PasswordResetTokenService passwordResetTokenService;

    public EmailServiceImpl(
            VerificationTokenService verificationTokenService,
            JavaMailSender javaMailSender,
            TemplateEngine templateEngine,
            AccountService accountService,
            PasswordResetTokenService passwordResetTokenService) {
        this.verificationTokenService = verificationTokenService;
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.accountService = accountService;
        this.passwordResetTokenService = passwordResetTokenService;
    }

    @Override
    public void sendVerificationEmail(Account account) throws MessagingException {

        VerificationToken verificationToken = verificationTokenService.findByAccountId(account.getId());

        if(verificationToken != null) {
            String token = verificationToken.getToken();

            Context context = new Context();
            context.setVariable("link", appHost + "active-email?token=" + token);

            String body = templateEngine.process("verification", context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(account.getEmail());
            mimeMessageHelper.setSubject("[Sweetagram] Please verify your email address.");
            mimeMessageHelper.setText(body, true);
            javaMailSender.send(mimeMessage);
        }

    }

    @Override
    public void sendResetPasswordEmail(String email) throws MessagingException {
        Account account = accountService.getAccountByEmail(email);
        PasswordResetToken passwordResetToken = passwordResetTokenService.findByAccountId(account.getId());

        if(passwordResetToken != null) {
            String token = passwordResetToken.getToken();

            Context context = new Context();
            context.setVariable("link", appHost + "reset-password?token=" + token);

            String body = templateEngine.process("password-reset", context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(account.getEmail());
            mimeMessageHelper.setSubject("[Sweetagram] Please reset your password.");
            mimeMessageHelper.setText(body, true);
            javaMailSender.send(mimeMessage);

        }
    }

}
