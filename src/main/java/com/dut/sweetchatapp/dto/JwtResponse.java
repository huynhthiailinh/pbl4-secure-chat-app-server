package com.dut.sweetchatapp.dto;

import com.dut.sweetchatapp.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class JwtResponse {
    private String token;

    private String type = "Bearer";

    private int id;

    private String fullName;

    private String username;

    private String email;

    private List<String> roles;

    private String avatar;

    private Gender gender;

}
