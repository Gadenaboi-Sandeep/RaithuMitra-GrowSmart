package com.raithumitra.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "equipment_bookings")
public class EquipmentBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer bookingId;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private User farmer;

    @Column(name = "booking_date")
    private LocalDate bookingDate;

    @Column(name = "hours_requested")
    private Integer hoursRequested;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.PENDING;

    public enum Status {
        PENDING, CONFIRMED, COMPLETED, REJECTED
    }
}
