package com.dut.sweetchatapp.repository;

import com.dut.sweetchatapp.model.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Optional;

@Transactional
@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {

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
    @Query("UPDATE Account a SET a.avatar = :avatar WHERE a.id = :id")
    void updateAvatarById(@Param("id") int id, @Param("avatar") String avatar);

}
