package com.raithumitra.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "labor_hiring")
public class LaborHiring {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hiring_id")
    private Integer hiringId;

    @ManyToOne
    @JoinColumn(name = "labor_id")
    private Laborer laborer;

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private User farmer;

    @Column(name = "work_start_date")
    private LocalDate workStartDate;

    @Column(name = "duration_days")
    private Integer durationDays;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.REQUESTED;

    public enum Status {
        REQUESTED, ACCEPTED, REJECTED
    }
}
