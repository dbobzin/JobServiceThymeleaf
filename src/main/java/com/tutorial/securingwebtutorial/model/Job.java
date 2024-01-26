package com.tutorial.securingwebtutorial.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private double payRate;
    private String jobType;
    private String location;
    private boolean benefits;
    @OneToMany(mappedBy = "job")
    private List<JobApplication> applications = new ArrayList<>();
}

