package com.dut.sweetchatapp.repository;

import com.dut.sweetchatapp.enums.RoleName;
import com.dut.sweetchatapp.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName (RoleName name);
}
