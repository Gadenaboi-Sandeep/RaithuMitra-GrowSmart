package com.raithumitra.data.local.entity;

public class LaborHiring {
    public int hiringId;
    public LaborerRef laborer;
    public String startDate; // Assuming date is needed, or just job details
    public String status;

    public static class LaborerRef {
        public int laborId;

        public LaborerRef(int id) {
            this.laborId = id;
        }
    }

    public LaborHiring(int laborerId, String startDate) {
        this.laborer = new LaborerRef(laborerId);
        this.startDate = startDate;
        this.status = "REQUESTED";
    }
}
