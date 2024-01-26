package com.tutorial.securingwebtutorial.service;

import com.tutorial.securingwebtutorial.model.ApplicationStatus;
import com.tutorial.securingwebtutorial.model.Job;
import com.tutorial.securingwebtutorial.model.JobApplication;
import com.tutorial.securingwebtutorial.model.User;
import com.tutorial.securingwebtutorial.repository.JobRepository;
import com.tutorial.securingwebtutorial.repository.applicationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private applicationRepository applicationRepository;

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    public Job updateJob(Long id, Job updatedJob) {
        if (jobRepository.existsById(id)) {
            updatedJob.setId(id);
            return jobRepository.save(updatedJob);
        } else {
            throw new IllegalArgumentException("Job with ID " + id + " not found.");
        }
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    public void applyForJob(Long jobId, User applicant) {
        // Check if the job exists
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found."));

        // Create a new job application
        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setApplicant(applicant);
        application.setStatus(ApplicationStatus.PENDING);

        // Save the application
        applicationRepository.save(application);
    }

    public List<JobApplication> getJobApplicationsForUser(User user) {
        // Retrieve job applications for a specific user
        return applicationRepository.findByApplicant(user);
    }

    public List<JobApplication> getAllJobApplications() {
        // Retrieve all job applications
        return applicationRepository.findAll();
    }

    public JobApplication getJobApplicationById(Long applicationId) {
        // Retrieve a specific job application by ID
        return applicationRepository.findById(applicationId)
                .orElse(null);
    }

    public void saveJobApplication(JobApplication application) {
        // Save a job application
        applicationRepository.save(application);
    }
}

