package com.tutorial.securingwebtutorial.repository;

import com.tutorial.securingwebtutorial.model.JobApplication;
import com.tutorial.securingwebtutorial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface applicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByApplicant(User applicant);
}
