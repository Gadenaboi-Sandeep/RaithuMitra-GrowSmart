package com.raithumitra.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "laborers")
public class Laborer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "labor_id")
    private Integer laborId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "skill_type", length = 100)
    private String skillType;

    @Column(name = "daily_wage", nullable = false)
    private String dailyWage;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "member_count")
    private Integer memberCount = 1; // Default to 1 (Individual)

    @Column(name = "is_available")
    private boolean isAvailable = true;
}
