package com.raithumitra.backend.controller;

import com.raithumitra.backend.model.EquipmentBooking;
import com.raithumitra.backend.repository.EquipmentBookingRepository;
import com.raithumitra.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class EquipmentBookingController {

    @Autowired
    private EquipmentBookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/received")
    public List<EquipmentBooking> getReceivedBookings(Principal principal) {
        String phoneNumber = principal.getName(); // Extracted from JWT
        return bookingRepository.findByEquipment_Owner_PhoneNumber(phoneNumber);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Integer id, @RequestBody String status) {
        return bookingRepository.findById(id).map(booking -> {
            try {
                booking.setStatus(EquipmentBooking.Status.valueOf(status.toUpperCase()));
                bookingRepository.save(booking);
                return ResponseEntity.ok("Status updated to " + status);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid status");
            }
        }).orElse(ResponseEntity.notFound().build());
    }
}
