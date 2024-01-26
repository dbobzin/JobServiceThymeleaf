package com.tutorial.securingwebtutorial.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserDto {

    private String email;
    private String password;
    private String role;
    private String fullname;

}
