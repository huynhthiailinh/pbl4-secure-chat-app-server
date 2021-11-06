package com.dut.sweetchatapp.model;

import com.dut.sweetchatapp.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    private String fullName;

    private String email;

    @JsonIgnore
    private String password;

    private Gender gender;

    @CreatedDate
    @Column(updatable = false)
    private Date createdDate = new Date(System.currentTimeMillis());

    @LastModifiedDate
    private Date updatedDate = new Date(System.currentTimeMillis());

    private boolean isDeleted;

    private String avatar;

    private boolean enabled;

    @JsonIgnore
    @Column(columnDefinition = "BLOB")
    private byte[] publicKey;

    @JsonIgnore
    @Column(columnDefinition = "BLOB")
    private byte[] privateKey;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "accounts_roles", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns =
    @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

}
