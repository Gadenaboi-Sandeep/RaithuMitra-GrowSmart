package com.raithumitra.data.remote.model;

import com.raithumitra.data.local.entity.Laborer;

public class LaborHiringResponse {
    private int hiringId;
    private Laborer laborer;
    private User farmer;
    private String workStartDate;
    private int durationDays;
    private String status;

    public int getHiringId() {
        return hiringId;
    }

    public Laborer getLaborer() {
        return laborer;
    }

    public User getFarmer() {
        return farmer;
    }

    public String getWorkStartDate() {
        return workStartDate;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public String getStatus() {
        return status;
    }
}
