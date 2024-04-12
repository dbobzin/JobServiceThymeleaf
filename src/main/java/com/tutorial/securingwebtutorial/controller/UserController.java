package com.tutorial.securingwebtutorial.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import com.tutorial.securingwebtutorial.dto.UserDto;
import com.tutorial.securingwebtutorial.model.ApplicationStatus;
import com.tutorial.securingwebtutorial.model.Job;
import com.tutorial.securingwebtutorial.model.JobApplication;
import com.tutorial.securingwebtutorial.model.User;
import com.tutorial.securingwebtutorial.service.JobService;
import com.tutorial.securingwebtutorial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class UserController {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private JobService jobService;


    @GetMapping("/register")
    public String getRegistrationPage(@ModelAttribute("user") UserDto userDto) {
        return "register";
    }


    @PostMapping("/register")
    public String saveUser(@ModelAttribute("user") UserDto userDto, Model model) {
        userService.save(userDto);
        model.addAttribute("message", "Registered Successfuly!");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

//    @GetMapping("/user-page") v1
//    public String userPage(Model model, Principal principal) {
//        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
//        model.addAttribute("user", userDetails);
//
//        // Additional logic to retrieve user-specific data if needed
//
//        // Fetch jobs for the user
//        List<Job> userJobs = jobService.getAllJobs();
//        model.addAttribute("userJobs", userJobs);
//
//        return "user";
//    }

    //v2
    @GetMapping("/user-page")
    public String userPage(Model model, Principal principal) {
        // Fetch the currently authenticated user
        User user = userService.getCurrentUser();
        // Additional logic to retrieve user-specific data if needed
        // Fetch jobs for the user
        List<Job> userJobs = jobService.getAllJobs();  // Replace with your logic to get user-specific jobs
        // Filter jobs user hasn't applied for
        List<Job> jobsNotApplied = userJobs.stream()
                .filter(job -> !jobService.hasUserAppliedForJob(job.getId(), user))
                .collect(Collectors.toList());
        model.addAttribute("userJobs", jobsNotApplied);
        model.addAttribute("user", user);

        return "user";
    }

    @PostMapping("/apply/{jobId}")
    public String applyForJob(@PathVariable Long jobId, Principal principal, RedirectAttributes attributes) {
        User applicant = userService.getCurrentUser();

        if (jobService.hasUserAppliedForJob(jobId, applicant)) {
            // You have already applied for this job
            attributes.addFlashAttribute("message", "You have already applied for this job.");
            attributes.addFlashAttribute("messageDuration", 5000); // Corrected to use attributes
        } else {
            // Process the application
            jobService.applyForJob(jobId, applicant);
            attributes.addFlashAttribute("message", "Application submitted successfully.");
        }

        return "redirect:/user-page";
    }

    @GetMapping("admin-page")
    public String adminPage(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        List<User> userList = userService.getAllUsers();
        model.addAttribute("userList", userList);
        List<Job> userJobs = jobService.getAllJobs();
        model.addAttribute("userJobs", userJobs);
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        // Get the logged-in user (admin)
        User admin = userService.getCurrentUser();
        // Check if the logged-in user is an admin
        if (!admin.getRole().equals("ADMIN")) {
            // Handle unauthorized access
            throw new AccessDeniedException("You don't have permission to view applications.");
        }

        // Retrieve and add all job applications directly to the model
        List<JobApplication> jobApplications = jobService.getAllJobApplications();
        model.addAttribute("jobApplications", jobApplications);
        return "admin";
    }

    @PostMapping("/admin/create-user")
    public String createUser(@RequestParam("email") String email,
                             @RequestParam("password") String password,
                             @RequestParam("fullname") String fullname,
                             @RequestParam("role") String role) {
        // Create a UserDto or directly call the service to save the user
        UserDto userDto = new UserDto(email, password, role, fullname);
        userService.save(userDto);
        return "redirect:/admin-page";
    }

    @GetMapping("/admin/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin-page";
    }

//    @PostMapping("/apply/{jobId}")
//    public ResponseEntity<String> applyForJob(@PathVariable Long jobId) {
//        // Get the logged-in user
//        User applicant = userService.getCurrentUser();
//
//        // Apply for the job
//        jobService.applyForJob(jobId, applicant);
//
//        return ResponseEntity.ok("Application submitted successfully.");
//    }

    //v1
//    @GetMapping("/approved-applications")
//    public String getApprovedApplications(Model model, Principal principal) {
//        User loggedInUser = userService.getUserByUsername(principal.getName());
//        List<JobApplication> approvedApplications = jobService.getJobApplicationsByUserAndStatus(loggedInUser, ApplicationStatus.APPROVED);
//        model.addAttribute("jobApplications", approvedApplications);
//        return "approved-applications";
//    }
//
//    @GetMapping("/denied-applications")
//    public String getDeniedApplications(Model model, Principal principal) {
//        User loggedInUser = userService.getUserByUsername(principal.getName());
//        List<JobApplication> deniedApplications = jobService.getJobApplicationsByUserAndStatus(loggedInUser, ApplicationStatus.DENIED);
//        model.addAttribute("jobApplications", deniedApplications);
//        return "denied-applications";
//    }
    @GetMapping("/applicationStatus")
    public String getApplicationStatus(Model model, Principal principal) {
        // Fetch the currently authenticated user
        User user = userService.getCurrentUser();

        // Additional logic to retrieve user-specific data if needed

        // Fetch approved and denied applications for the user
        List<JobApplication> approvedApplications = jobService.getJobApplicationsByUserAndStatus(user, ApplicationStatus.APPROVED);
        List<JobApplication> deniedApplications = jobService.getJobApplicationsByUserAndStatus(user, ApplicationStatus.DENIED);

        model.addAttribute("user", user);
        model.addAttribute("approvedApplications", approvedApplications);
        model.addAttribute("deniedApplications", deniedApplications);

        return "applicationStatus";
    }

    @PostMapping("/delete-applications")
    public String deleteApplications(Model model, Principal principal, RedirectAttributes attributes) {
        User user = userService.getCurrentUser();

        jobService.deleteApprovedAndDeniedApplicationsForUser(user);

        // Add success flash attribute
        attributes.addFlashAttribute("message", "You deleted all applications successfully.");
        attributes.addFlashAttribute("messageDuration", 3000);

        return "redirect:/applicationStatus";
    }

    @PostMapping("/delete-application/{id}")
    public String deleteApplication(@PathVariable Long id, Model model, Principal principal, RedirectAttributes attributes) {
        // Implement the logic to delete the specific application with the given ID
        jobService.deleteApplicationById(id);

        // Add success flash attribute
        attributes.addFlashAttribute("message", "You deleted the application successfully.");
        attributes.addFlashAttribute("messageDuration", 3000);

        return "redirect:/applicationStatus";
    }
}
