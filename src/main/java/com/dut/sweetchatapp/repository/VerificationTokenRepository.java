package com.dut.sweetchatapp.repository;

import com.dut.sweetchatapp.model.VerificationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Integer> {

    VerificationToken findByToken(String token);

    VerificationToken findByAccountId(int accountId);

    boolean existsByAccountId(int accountId);

}
