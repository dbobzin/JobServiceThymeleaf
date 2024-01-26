package com.tutorial.securingwebtutorial.controller;

import com.tutorial.securingwebtutorial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordResetController {

    @Autowired
    private UserService userService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email, Model model) {
        // Generate a reset token, save it in the database, and send an email
        userService.initiatePasswordReset(email);
        model.addAttribute("message", "Password reset email sent. Check your inbox.");
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        // Verify the token and its expiration time
        if (userService.isPasswordResetTokenValid(token)) {
            model.addAttribute("token", token);
            return "reset-password";
        } else {
            model.addAttribute("error", "Invalid or expired token");
            return "redirect:/forgot-password";
        }
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam("token") String token,
                                      @RequestParam("password") String password,
                                      Model model) {
        // Reset the password if the token is valid
        if (userService.isPasswordResetTokenValid(token)) {
            userService.resetPassword(token, password);
            model.addAttribute("message", "Password reset successful. You can now log in with your new password.");
            return "login";
        } else {
            model.addAttribute("error", "Invalid or expired token");
            return "redirect:/forgot-password";
        }
    }
}

