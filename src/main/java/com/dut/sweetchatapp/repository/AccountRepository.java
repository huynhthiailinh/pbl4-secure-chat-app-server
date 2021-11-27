package com.dut.sweetchatapp.repository;

import com.dut.sweetchatapp.enums.Gender;
import com.dut.sweetchatapp.model.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {

    @Query("SELECT a.avatar FROM Account a WHERE a.id = :accountId")
    String getAvatarByAccountId(@Param("accountId") int accountId);

    Optional<Account> findByUsernameOrEmail(String username, String email);

    @Query("SELECT a FROM Account a WHERE a.email = :email")
    Account findByEmail(@Param("email") String email);

    Boolean existsByUsername(String name);

    Boolean existsByEmail(String email);

    @Query("SELECT a.publicKey FROM Account a WHERE a.id = :accountId")
    byte[] getPublicKeyByAccountId(@Param("accountId") int accountId);

    @Query("SELECT a.privateKey FROM Account a WHERE a.id = :accountId")
    byte[] getPrivateKeyByAccountId(@Param("accountId") int accountId);

    @Modifying
    @Query("UPDATE Account a SET a.fullName = :fullName, a.email = :email, " +
            "a.gender = :gender, a.avatar = :avatar WHERE a.id = :accountId")
    void updateProfile(@Param("fullName") String fullName,
                       @Param("email") String email,
                       @Param("gender") Gender gender,
                       @Param("avatar") String avatar,
                       @Param("accountId") int accountId);

}
