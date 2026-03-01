package com.raithumitra.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "contracts")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id")
    private Integer contractId;

    @ManyToOne
    @JoinColumn(name = "corporate_id")
    private User corporate;

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private User farmer;

    @Column(name = "crop_name", nullable = false, length = 50)
    private String cropName;

    @Column(name = "price_per_kg", nullable = false)
    private BigDecimal pricePerKg;

    @Column(name = "quantity_required", nullable = false)
    private Integer quantityRequired;

    @Column(name = "contract_details", columnDefinition = "TEXT")
    private String contractDetails;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.OPEN;

    public enum Status {
        OPEN, SIGNED, COMPLETED, CANCELLED
    }
}
