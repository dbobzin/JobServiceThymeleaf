package com.tutorial.securingwebtutorial.controller;

import com.tutorial.securingwebtutorial.model.ApplicationStatus;
import com.tutorial.securingwebtutorial.model.JobApplication;
import com.tutorial.securingwebtutorial.model.User;
import com.tutorial.securingwebtutorial.service.JobService;
import com.tutorial.securingwebtutorial.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        List<JobApplication> pendingApplications = jobService.getJobApplicationsByStatus(ApplicationStatus.PENDING);
        model.addAttribute("jobApplications", pendingApplications);
        return "applications";
    }
//    @PostMapping("/respond/{applicationId}")
//    public String respondToApplication(@PathVariable Long applicationId,
//                                       @RequestParam String response) {
//        // Get the logged-in user (admin)
////        User admin = userService.getCurrentUser();
//
//        // Check if the logged-in user is an admin
////        if (!admin.getRole().equals("ADMIN")) {
////            // Handle unauthorized access
////            throw new AccessDeniedException("You don't have permission to respond to applications.");
////        }
//
//        // Retrieve the job application
//        JobApplication application = jobService.getJobApplicationById(applicationId);
//
//        // Handle application not found
//        if (application == null) {
//            throw new EntityNotFoundException("Job application not found.");
//        }
//
//        // Update the application status based on the response
//        if ("approve".equalsIgnoreCase(response)) {
//            application.setStatus(ApplicationStatus.APPROVED);
//        } else if ("deny".equalsIgnoreCase(response)) {
//            application.setStatus(ApplicationStatus.DENIED);
//        } else {
//            // Handle invalid response
//            throw new IllegalArgumentException("Invalid response value.");
//        }
//
//        // Save the updated application
//        jobService.saveJobApplication(application);
//
//        return "redirect:/admin/applications";
//    }

    @GetMapping("/approve/{applicationId}")
    public String approveApplication(@PathVariable Long applicationId, RedirectAttributes redirectAttributes) {
        return updateApplicationStatus(applicationId, ApplicationStatus.APPROVED, redirectAttributes);
    }

    @GetMapping("/deny/{applicationId}")
    public String denyApplication(@PathVariable Long applicationId, RedirectAttributes redirectAttributes) {
        return updateApplicationStatus(applicationId, ApplicationStatus.DENIED, redirectAttributes);
    }

    private String updateApplicationStatus(Long applicationId, ApplicationStatus status, RedirectAttributes redirectAttributes) {
        JobApplication application = jobService.getJobApplicationById(applicationId);

        if (application == null) {
            return "redirect:/admin/applications";
        }

        application.setStatus(status);
        jobService.saveJobApplication(application);
        redirectAttributes.addFlashAttribute("success", "Application " + status.toString().toLowerCase() + " successfully");
        return "redirect:/admin/applications";
    }

}

