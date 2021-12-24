package com.dut.sweetchatapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String token;

    private int accountId;

    private Date expiryDate;

    public PasswordResetToken(String token, int accountId) {
        this.token = token;
        this.accountId = accountId;
        this.expiryDate = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(3));
    }

}
