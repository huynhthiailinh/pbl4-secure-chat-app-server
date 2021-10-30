package com.dut.sweetchatapp.repository;

import com.dut.sweetchatapp.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {

    Optional<Account> findByUsernameOrEmail(String username, String email);

    Boolean existsByUsername(String name);

    Boolean existsByEmail(String email);

}
