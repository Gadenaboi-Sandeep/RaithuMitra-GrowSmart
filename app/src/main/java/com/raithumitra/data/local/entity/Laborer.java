package com.raithumitra.data.local.entity;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "laborers")
public class Laborer {
    @PrimaryKey(autoGenerate = true)
    public int laborId;

    @Embedded(prefix = "user_")
    public User user;

    public String skillType;
    public String dailyWage; // Backend: BigDecimal -> String ok for JSON if simple, but better checking
    public int rating = 4; // Default
    public int memberCount = 1;

    public Laborer(int laborId, User user, String skillType, String dailyWage) {
        this.laborId = laborId;
        this.user = user;
        this.skillType = skillType;
        this.dailyWage = dailyWage;
    }

    // Constructor for Room
    @Ignore
    public Laborer(User user, String skillType, String dailyWage, int rating) {
        this.user = user;
        this.skillType = skillType;
        this.dailyWage = dailyWage;
        this.rating = rating;
    }
}
