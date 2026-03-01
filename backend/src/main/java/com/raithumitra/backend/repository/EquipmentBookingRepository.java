package com.raithumitra.backend.repository;

import com.raithumitra.backend.model.EquipmentBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EquipmentBookingRepository extends JpaRepository<EquipmentBooking, Integer> {
    List<EquipmentBooking> findByEquipment_Owner_PhoneNumber(String phoneNumber);
}
