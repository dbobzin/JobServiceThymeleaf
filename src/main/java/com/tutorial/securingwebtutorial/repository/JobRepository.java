package com.tutorial.securingwebtutorial.repository;
import com.tutorial.securingwebtutorial.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("SELECT COUNT(ja) > 0 FROM JobApplication ja WHERE ja.job.id = :jobId AND ja.applicant.id = :userId")
    boolean hasUserApplied(@Param("jobId") Long jobId, @Param("userId") Long userId);
}

