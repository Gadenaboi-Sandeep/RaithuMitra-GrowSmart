package com.raithumitra.backend.controller;

import com.raithumitra.backend.model.AuthRequest;
import com.raithumitra.backend.model.AuthResponse;
import com.raithumitra.backend.model.User;
import com.raithumitra.backend.repository.UserRepository;
import com.raithumitra.backend.service.MyUserDetailsService;
import com.raithumitra.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AuthRequest authRequest) {
        System.out.println("Signup Request: " + authRequest.getPhoneNumber() + ", Role: " + authRequest.getRole());

        if (userRepository.existsByPhoneNumber(authRequest.getPhoneNumber())) {
            return ResponseEntity.badRequest().body("Error: Phone number is already taken!");
        }

        User user = new User();
        user.setFullName(authRequest.getFullName());
        user.setPhoneNumber(authRequest.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setRole(User.Role.valueOf(authRequest.getRole()));
        user.setAddress(authRequest.getAddress());

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        if (!userRepository.existsByPhoneNumber(authRequest.getPhoneNumber())) {
            return ResponseEntity.badRequest().body("Error: User not found");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getPhoneNumber(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Error: Incorrect password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getPhoneNumber());
        final String jwt = jwtUtil.generateToken(userDetails);

        User user = userRepository.findByPhoneNumber(authRequest.getPhoneNumber()).orElseThrow();

        return ResponseEntity.ok(new AuthResponse(
                jwt,
                user.getRole().name(),
                "Login successful",
                user.getFullName(),
                user.getPhoneNumber(),
                user.getAddress()));
    }
}
