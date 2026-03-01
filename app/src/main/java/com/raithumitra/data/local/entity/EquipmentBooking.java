package com.raithumitra.data.local.entity;

public class EquipmentBooking {
    public int bookingId;

    // Backend EquipmentBooking: private Equipment equipment;
    // Checking backend controller again: booking.setFarmer(currentUser);
    // bookingRepository.save(booking);
    // Standard JPA expects "equipment" object.

    // Simplification: We will send an object that structure matches what Backend
    // expects.
    // If Backend uses standard deserialization, we need to send { "equipment": {
    // "equipmentId": 123 } }

    public EquipmentRef equipment;
    public String startDate;
    public String endDate;
    public String status;

    public static class EquipmentRef {
        public int equipmentId;

        public EquipmentRef(int id) {
            this.equipmentId = id;
        }
    }

    public EquipmentBooking(int equipmentId, String startDate) {
        this.equipment = new EquipmentRef(equipmentId);
        this.startDate = startDate;
        this.endDate = startDate; // Single day booking for now
        this.status = "PENDING";
    }
}
