package com.raithumitra.backend.controller;

import com.raithumitra.backend.model.Equipment;
import com.raithumitra.backend.model.EquipmentBooking;
import com.raithumitra.backend.model.User;
import com.raithumitra.backend.repository.EquipmentBookingRepository;
import com.raithumitra.backend.repository.EquipmentRepository;
import com.raithumitra.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentBookingRepository equipmentBookingRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Equipment> getAllEquipment() {
        List<Equipment> equipmentList = equipmentRepository.findAll();
        for (Equipment e : equipmentList) {
            System.out.println("Equipment: " + e.getEquipmentName() + ", ImageURL: " + e.getImageUrl());
        }
        return equipmentList;
    }

    @PostMapping
    public ResponseEntity<?> addEquipment(@RequestBody Equipment equipment) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByPhoneNumber(userDetails.getUsername()).orElseThrow();

        equipment.setOwner(currentUser);
        return ResponseEntity.ok(equipmentRepository.save(equipment));
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookEquipment(@RequestBody EquipmentBooking booking) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByPhoneNumber(userDetails.getUsername()).orElseThrow();

        booking.setFarmer(currentUser);
        booking.setBookingDate(LocalDate.now());
        booking.setStatus(EquipmentBooking.Status.PENDING);

        return ResponseEntity.ok(equipmentBookingRepository.save(booking));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEquipment(@PathVariable Integer id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByPhoneNumber(userDetails.getUsername()).orElseThrow();
        Equipment equipment = equipmentRepository.findById(id).orElseThrow();

        if (equipment.getOwner().getId().equals(currentUser.getId())) {
            equipmentRepository.delete(equipment);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}
