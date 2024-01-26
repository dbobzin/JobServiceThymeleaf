package com.tutorial.securingwebtutorial.repository;
import com.tutorial.securingwebtutorial.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}

