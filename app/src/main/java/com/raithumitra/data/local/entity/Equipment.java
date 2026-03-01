package com.raithumitra.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "equipment")
public class Equipment {
    @PrimaryKey(autoGenerate = true)
    public int equipmentId;

    public String equipmentName; // e.g. "John Deere 5050"
    public String category; // e.g. "TRACTOR", "DRONE" (Matches Backend Enum)
    public String hourlyRate; // e.g. "800" (Matches Backend BigDecimal)
    public String ownerName;
    public String location; // e.g. "Warangal, 5km away"
    public String imageUrl;
    public boolean isAvailable;
    public String ownerPhone;

    // Constructor
    public Equipment(String equipmentName, String category, String hourlyRate, String ownerName, String location,
            String ownerPhone) {
        this.equipmentName = equipmentName;
        this.category = category;
        this.hourlyRate = hourlyRate;
        this.ownerName = ownerName;
        this.location = location;
        this.imageUrl = null;
        this.isAvailable = true;
        this.ownerPhone = ownerPhone;
    }
}
