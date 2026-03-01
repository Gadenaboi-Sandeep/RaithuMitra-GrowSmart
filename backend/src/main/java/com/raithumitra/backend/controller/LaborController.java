package com.raithumitra.backend.controller;

import com.raithumitra.backend.model.LaborHiring;
import com.raithumitra.backend.model.Laborer;
import com.raithumitra.backend.model.User;
import com.raithumitra.backend.repository.LaborHiringRepository;
import com.raithumitra.backend.repository.LaborerRepository;
import com.raithumitra.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labor")
public class LaborController {

    @Autowired
    private LaborerRepository laborerRepository;

    @Autowired
    private LaborHiringRepository laborHiringRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Laborer> getAllLaborers() {
        return laborerRepository.findAll();
    }

    @PostMapping("/profile")
    public ResponseEntity<?> createLaborerProfile(@RequestBody Laborer laborer) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByPhoneNumber(userDetails.getUsername()).orElseThrow();

        // Check if profile exists
        return laborerRepository.findByUser(currentUser)
                .map(existingLaborer -> {
                    // Update existing
                    existingLaborer.setSkillType(laborer.getSkillType());
                    existingLaborer.setDailyWage(laborer.getDailyWage());
                    existingLaborer.setMemberCount(laborer.getMemberCount());
                    // existingLaborer.setExperienceYears(laborer.getExperienceYears()); // If added
                    // to UI
                    return ResponseEntity.ok(laborerRepository.save(existingLaborer));
                })
                .orElseGet(() -> {
                    // Create new
                    laborer.setUser(currentUser);
                    return ResponseEntity.ok(laborerRepository.save(laborer));
                });
    }

    @PostMapping("/hire")
    public ResponseEntity<?> hireLaborer(@RequestBody LaborHiring hiring) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByPhoneNumber(userDetails.getUsername()).orElseThrow();

        hiring.setFarmer(currentUser);
        hiring.setStatus(LaborHiring.Status.REQUESTED);

        return ResponseEntity.ok(laborHiringRepository.save(hiring));
    }

    @GetMapping("/received")
    public List<LaborHiring> getReceivedRequests(java.security.Principal principal) {
        String phoneNumber = principal.getName();
        return laborHiringRepository.findByLaborer_User_PhoneNumber(phoneNumber);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateHiringStatus(@PathVariable Integer id, @RequestBody String status) {
        return laborHiringRepository.findById(id).map(hiring -> {
            try {
                hiring.setStatus(LaborHiring.Status.valueOf(status.toUpperCase()));
                laborHiringRepository.save(hiring);
                return ResponseEntity.ok("Status updated to " + status);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid status");
            }
        }).orElse(ResponseEntity.notFound().build());
    }
}
