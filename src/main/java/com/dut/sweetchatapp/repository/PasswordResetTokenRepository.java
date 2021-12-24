package com.dut.sweetchatapp.repository;

import com.dut.sweetchatapp.model.PasswordResetToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Integer> {

    PasswordResetToken findByAccountId(@Param("accountId") int accountId);

    PasswordResetToken findByToken(@Param("token") String token);

    Boolean existsByToken(String token);

    Boolean existsByAccountId(int accountId);

    @Modifying
    @Query("UPDATE PasswordResetToken p SET p.token = '' WHERE p.token = :token")
    void deleteByToken(@Param("token") String token);

}
