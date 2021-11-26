package com.dut.sweetchatapp.controller;

import com.dut.sweetchatapp.dto.JwtResponse;
import com.dut.sweetchatapp.dto.LoginRequest;
import com.dut.sweetchatapp.dto.MessageResponse;
import com.dut.sweetchatapp.dto.SignupRequest;
import com.dut.sweetchatapp.handleException.exception.InactiveEmailException;
import com.dut.sweetchatapp.handleException.exception.InvalidParamException;
import com.dut.sweetchatapp.model.Account;
import com.dut.sweetchatapp.model.Role;
import com.dut.sweetchatapp.repository.RoleRepository;
import com.dut.sweetchatapp.rsa.RSAKey;
import com.dut.sweetchatapp.sercurity.JwtUtils;
import com.dut.sweetchatapp.service.AccountService;
import com.dut.sweetchatapp.service.EmailService;
import com.dut.sweetchatapp.service.VerificationTokenService;
import com.dut.sweetchatapp.service.impl.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dut.sweetchatapp.constant.DefaultPath.AUTHENTICATION_PATH;
import static com.dut.sweetchatapp.enums.RoleName.*;
import static com.dut.sweetchatapp.rsa.SecurityKeyPairGenerator.generateKeyPair;

@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(AUTHENTICATION_PATH)
public class AuthController {
    public final AuthenticationManager authenticationManager;

    private final AccountService accountService;

    public final RoleRepository roleRepository;

    public final JwtUtils jwtUtils;

    public final PasswordEncoder passwordEncoder;

    public final VerificationTokenService verificationTokenService;

    public final EmailService emailService;

    @PostMapping("sign-in")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginRequest loginRequest) {
        if (!accountService.existsByUsername(loginRequest.getUsername()) && !accountService.existsByEmail(loginRequest.getUsername())) {
            throw new InvalidParamException("Error: Username or email does not exists!");
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl accountDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = accountDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(JwtResponse
                .builder()
                .token(jwt)
                .id(accountDetails.getId())
                .fullName(accountDetails.getFullName())
                .username(accountDetails.getUsername())
                .email(accountDetails.getEmail())
                .roles(roles)
                .avatar(accountDetails.getAvatar())
                .gender(accountDetails.getGender())
                .build());
    }

    @PostMapping("sign-up")
    public ResponseEntity<?> registerUser(@Validated @RequestBody SignupRequest signupRequest) throws NoSuchAlgorithmException {
        boolean isNewAccount = true;

        Account user = Account.builder().build();

        if (accountService.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (accountService.existsByEmail(signupRequest.getEmail())) {
            if (accountService.checkIfEnabled(signupRequest.getEmail())) {
                throw new InactiveEmailException("Error: Email is already taken!");
            } else {
                isNewAccount = false;
                user = accountService.getAccountByEmail(signupRequest.getEmail());
                user.setUsername(signupRequest.getUsername());
                user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
                user.setFullName(signupRequest.getFullName());
            }
        }

        if (!isValidEmailAddress(signupRequest.getEmail())) {
            throw new InvalidParamException("Error: Email is incorrect!");
        }

        if (isNewAccount) {

            RSAKey rsaKey = generateKeyPair();

            user = Account.builder()
                    .username(signupRequest.getUsername())
                    .fullName(signupRequest.getFullName())
                    .password(passwordEncoder.encode(signupRequest.getPassword()))
                    .email(signupRequest.getEmail())
                    .publicKey(rsaKey.getPublicKey().getEncoded())
                    .privateKey(rsaKey.getPrivateKey().getEncoded())
                    .build();

            Set<String> strRoles = signupRequest.getRole();
            Set<Role> roles = new HashSet<>();

            if (strRoles == null) {
                Role accountRole = roleRepository.findByName(ROLE_USER).orElseThrow(() ->
                        new RuntimeException("Error: Role is not found"));
                roles.add(accountRole);
            } else {
                strRoles.forEach(role -> {
                    switch (role) {
                        case "admin":
                            Role adminRole = roleRepository.findByName(ROLE_ADMIN).orElseThrow(() -> new
                                    RuntimeException("Error: Role is not found"));
                            roles.add(adminRole);
                            break;
                        default:
                            Role userRole = roleRepository.findByName(ROLE_USER).orElseThrow(() -> new
                                    RuntimeException("Error: Role is not found"));
                            roles.add(userRole);
                            break;
                    }
                });
            }
            user.setRoles(roles);
            user.setEnabled(false);
            accountService.save(user);
        }

        Optional<Account> saved = Optional.of(accountService.save(user));

        saved.ifPresent(u -> {
            try {
                verificationTokenService.save(saved.get().getId());

                //send verification email
                emailService.sendVerificationEmail(u);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return ResponseEntity.ok(new MessageResponse("Success! A verification email has been sent to your email address"));

    }

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
