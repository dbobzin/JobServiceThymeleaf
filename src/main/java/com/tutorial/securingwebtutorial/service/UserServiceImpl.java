package com.tutorial.securingwebtutorial.service;

import com.tutorial.securingwebtutorial.dto.UserDto;
import com.tutorial.securingwebtutorial.model.PasswordResetToken;
import com.tutorial.securingwebtutorial.model.User;
import com.tutorial.securingwebtutorial.repository.PasswordResetTokenRepository;
import com.tutorial.securingwebtutorial.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

//    @Autowired
//    private EmailService emailService;

    @Override
    public User save(UserDto userDto) {
        if (userDto.getRole() == null || userDto.getRole().isEmpty()) {
            userDto.setRole("USER");
        }

        User user = new User(userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()), userDto.getRole(), userDto.getFullname());

        user.setEnabled(true);

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void initiatePasswordReset(String email) {
        // Find the user by email
        User user = userRepository.findByEmail(email);

        if (user != null) {
            // Generate a unique token
            String resetToken = UUID.randomUUID().toString();

            // Set an expiration time (e.g., 1 hour from now)
            LocalDateTime expirationTime = LocalDateTime.now().plusHours(1);

            // Save the token and user information in the database
            PasswordResetToken passwordResetToken = new PasswordResetToken();
            passwordResetToken.setToken(resetToken);
            passwordResetToken.setExpirationTime(expirationTime);
            passwordResetToken.setUser(user);
            passwordResetTokenRepository.save(passwordResetToken);

            // Send an email to the user with a link containing the token
            String resetLink = "https://yourapp.com/reset-password?token=" + resetToken;
            String emailContent = "Click the following link to reset your password: " + resetLink;

            // Replace this with your actual email sending logic
//            emailService.sendEmail(user.getEmail(), "Password Reset", emailContent);
        }
        // Note: If the email doesn't match any user, you might want to handle this case accordingly.
    }

    @Override
    public boolean isPasswordResetTokenValid(String token) {
        // Find the token in the database
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

        // Check if the token exists and hasn't expired
        return passwordResetToken != null && !isTokenExpired(passwordResetToken);
    }

    // Helper method to check if a token has expired
    private boolean isTokenExpired(PasswordResetToken passwordResetToken) {
        LocalDateTime expirationTime = passwordResetToken.getExpirationTime();
        return expirationTime.isBefore(LocalDateTime.now());
    }
    @Override
    public void resetPassword(String token, String newPassword) {
        // Find the token in the database
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

        // Check if the token is valid (exists and hasn't expired)
        if (isTokenValid(passwordResetToken)) {
            // Update the user's password
            User user = passwordResetToken.getUser();
            user.setPassword(passwordEncoder.encode(newPassword));

            // Save the updated user information
            userRepository.save(user);

            // Remove the used password reset token from the database
            passwordResetTokenRepository.delete(passwordResetToken);
        } else {
            System.out.println("ERROR: invalid token or expired");
            // Handle invalid or expired token
//            throw new InvalidTokenException("Invalid or expired password reset token");
        }
    }

    private boolean isTokenValid(PasswordResetToken passwordResetToken) {
        return passwordResetToken != null && !isTokenExpired(passwordResetToken);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();

            // Use the existing userRepository bean to get the user by email
            return userRepository.findByEmail(userEmail);
        }

        return null; // Handle the case where the current user is not available
    }


}

