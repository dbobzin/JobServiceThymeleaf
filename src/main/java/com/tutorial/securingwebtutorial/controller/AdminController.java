package com.tutorial.securingwebtutorial.controller;

import com.tutorial.securingwebtutorial.model.ApplicationStatus;
import com.tutorial.securingwebtutorial.model.JobApplication;
import com.tutorial.securingwebtutorial.model.User;
import com.tutorial.securingwebtutorial.service.JobService;
import com.tutorial.securingwebtutorial.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private JobService jobService;

    @Autowired
    private UserService userService;

//    @GetMapping("/applications")
//    public String viewApplications(Model model, Principal principal) {
//        // Get the logged-in user (admin)
////        User admin = userService.getCurrentUser();
//
//        // Check if the logged-in user is an admin
////        if (!admin.getRole().equals("ADMIN")) {
//            // Handle unauthorized access
////            throw new AccessDeniedException("You don't have permission to view applications.");
////        }
//
//        // Retrieve and add all job applications to the model
//        List<JobApplication> jobApplications = jobService.getAllJobApplications();
//        model.addAttribute("jobApplications", jobApplications);
//
//        return "applications";
//    }

    @GetMapping("/applications")
    public String getApplications(Model model) {
        List<JobApplication> jobApplications = jobService.getAllJobApplications();
        model.addAttribute("jobApplications", jobApplications);
        return "applications";
    }

    @PostMapping("/respond/{applicationId}")
    public String respondToApplication(@PathVariable Long applicationId,
                                       @RequestParam String response) {
        // Get the logged-in user (admin)
//        User admin = userService.getCurrentUser();

        // Check if the logged-in user is an admin
//        if (!admin.getRole().equals("ADMIN")) {
//            // Handle unauthorized access
//            throw new AccessDeniedException("You don't have permission to respond to applications.");
//        }

        // Retrieve the job application
        JobApplication application = jobService.getJobApplicationById(applicationId);

        // Handle application not found
        if (application == null) {
            throw new EntityNotFoundException("Job application not found.");
        }

        // Update the application status based on the response
        if ("approve".equalsIgnoreCase(response)) {
            application.setStatus(ApplicationStatus.APPROVED);
        } else if ("deny".equalsIgnoreCase(response)) {
            application.setStatus(ApplicationStatus.DENIED);
        } else {
            // Handle invalid response
            throw new IllegalArgumentException("Invalid response value.");
        }

        // Save the updated application
        jobService.saveJobApplication(application);

        return "redirect:/admin/applications";
    }

}

