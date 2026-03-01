package com.raithumitra.backend.controller;

import com.raithumitra.backend.model.Contract;
import com.raithumitra.backend.model.User;
import com.raithumitra.backend.repository.ContractRepository;
import com.raithumitra.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createContract(@RequestBody Contract contract) {
        // Automatically set corporate ID from logged in user if role is CORPORATE
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByPhoneNumber(userDetails.getUsername()).orElseThrow();

        if (currentUser.getRole() == User.Role.CORPORATE) {
            contract.setCorporate(currentUser);
        }

        contract.setStartDate(LocalDate.now());
        contract.setStatus(Contract.Status.OPEN);

        return ResponseEntity.ok(contractRepository.save(contract));
    }

    // Additional endpoint to sign a contract
    @PutMapping("/{id}/sign")
    public ResponseEntity<?> signContract(@PathVariable int id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByPhoneNumber(userDetails.getUsername()).orElseThrow();

        if (currentUser.getRole() == User.Role.FARMER) {
            contract.setFarmer(currentUser);
            contract.setStatus(Contract.Status.SIGNED);
            return ResponseEntity.ok(contractRepository.save(contract));
        } else {
            return ResponseEntity.badRequest().body("Only farmers can sign contracts");
        }
    }
}
