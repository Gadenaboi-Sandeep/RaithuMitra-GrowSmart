package com.raithumitra.data.remote.model;

import com.raithumitra.data.local.entity.Equipment;

public class EquipmentBookingResponse {
    private int bookingId;
    private Equipment equipment;
    private User farmer;
    private String status;
    private String bookingDate;

    public int getBookingId() {
        return bookingId;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public User getFarmer() {
        return farmer;
    }

    public String getStatus() {
        return status;
    }

    public String getBookingDate() {
        return bookingDate;
    }
}
