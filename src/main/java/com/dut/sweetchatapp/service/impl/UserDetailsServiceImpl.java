package com.dut.sweetchatapp.service.impl;

import com.dut.sweetchatapp.model.Account;
import com.dut.sweetchatapp.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    final AccountRepository userRepository;

    public UserDetailsServiceImpl(AccountRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(() -> new UsernameNotFoundException
                ("Account not found with username or email " + username));
        return UserDetailsImpl.build(user);
    }
}
