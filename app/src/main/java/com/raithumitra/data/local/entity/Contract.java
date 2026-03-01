package com.raithumitra.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contracts")
public class Contract {
    @PrimaryKey(autoGenerate = true)
    public int contractId;

    public String cropName;
    public String pricePerKg; // Backend: BigDecimal
    public int quantityRequired; // Backend: Integer
    public String contractDetails; // Backend: TEXT
    public String status; // Backend: Enum
    public String startDate;
    public String endDate;
    public String companyName; // Local helper/Transient
    public String location; // Local/Transient (Backend might not have this? Checking Controller)

    public Contract(String cropName, String pricePerKg, int quantityRequired, String contractDetails, String location) {
        this.cropName = cropName;
        this.pricePerKg = pricePerKg;
        this.quantityRequired = quantityRequired;
        this.contractDetails = contractDetails;
        this.location = location;
        this.status = "OPEN";
    }
}
