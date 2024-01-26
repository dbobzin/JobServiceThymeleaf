package com.tutorial.securingwebtutorial.service;

import com.tutorial.securingwebtutorial.dto.UserDto;
import com.tutorial.securingwebtutorial.model.User;

import java.util.List;

public interface UserService {
    User getCurrentUser();
    User save (UserDto userDto);
    void deleteUser(Long id);
    List<User> getAllUsers();

    void initiatePasswordReset(String email);
    boolean isPasswordResetTokenValid(String token);
    void resetPassword(String token, String newPassword);
}