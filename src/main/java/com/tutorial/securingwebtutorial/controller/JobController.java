package com.tutorial.securingwebtutorial.controller;

import com.tutorial.securingwebtutorial.model.Job;
import com.tutorial.securingwebtutorial.repository.UserRepository;
import com.tutorial.securingwebtutorial.service.JobService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping("/{id}")
    public Job getJobById(@PathVariable Long id) {
        return jobService.getJobById(id).orElse(null);
    }

    @PostMapping("/create")
    public void createJob(@RequestParam("description") String description,
                            @RequestParam("payRate") double payRate,
                            @RequestParam("jobType") String jobType,
                            @RequestParam("location") String location,
                            @RequestParam(name = "benefits", defaultValue = "false") boolean benefits,
                            HttpServletResponse response) throws IOException {
        // Create a Job object and populate its fields using the form parameters
        Job newJob = new Job();
        newJob.setDescription(description);
        newJob.setPayRate(payRate);
        newJob.setJobType(jobType);
        newJob.setLocation(location);
        newJob.setBenefits(benefits);

        jobService.createJob(newJob);
        response.sendRedirect("/user-page");
    }


    @PutMapping("/update/{id}")
    public Job updateJob(@PathVariable Long id, @RequestBody Job updatedJob) {
        return jobService.updateJob(id, updatedJob);
    }

//    @GetMapping("/delete/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public void deleteJob(@PathVariable Long id, HttpServletResponse response) throws IOException {
//        jobService.deleteJob(id);
//    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteJob(@PathVariable Long id, Authentication authentication, HttpServletResponse response) throws IOException {
        jobService.deleteJob(id);

        // Check the role of the authenticated user
        if (isAdmin(authentication)) {
            // Admins are redirected to the admin page
            response.sendRedirect("/admin-page");
        } else {
            // Users are redirected to the user page
            response.sendRedirect("/user-page");
        }
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));
    }

}

