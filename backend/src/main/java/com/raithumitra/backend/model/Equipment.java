package com.raithumitra.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "equipment")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private Integer equipmentId;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "equipment_name", nullable = false, length = 100)
    private String equipmentName;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "hourly_rate", nullable = false)
    private BigDecimal hourlyRate;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_available")
    private boolean isAvailable = true;

    @Column(name = "location", length = 100)
    private String location;

    public enum Category {
        TRACTOR, DRONE, HARVESTER, PLOUGH
    }

    public String getOwnerPhone() {
        return owner != null ? owner.getPhoneNumber() : null;
    }
}
