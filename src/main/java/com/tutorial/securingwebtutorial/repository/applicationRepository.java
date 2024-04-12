package com.tutorial.securingwebtutorial.repository;

import com.tutorial.securingwebtutorial.model.ApplicationStatus;
import com.tutorial.securingwebtutorial.model.JobApplication;
import com.tutorial.securingwebtutorial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface applicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByApplicant(User applicant);
    List<JobApplication> findByJobId(Long jobId);
    List<JobApplication> findByStatus(ApplicationStatus status);
    List<JobApplication> findByApplicantAndStatus(User applicant, ApplicationStatus status);
}
