package com.UMS.UMSBackend.controller;

import com.UMS.UMSBackend.auth.JwtTokenUtil;
import com.UMS.UMSBackend.entity.JwtRequest;
import com.UMS.UMSBackend.entity.User;
import com.UMS.UMSBackend.repository.UserRepository;
import com.UMS.UMSBackend.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/auth/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> registerUser(@RequestBody JwtRequest registrationRequest) {
        // Check if user already exists
        if (userDetailsService.loadUserByUsername(registrationRequest.getUsername()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }

        // Create new user
        User newUser = new User();
        newUser.setUsername(registrationRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        // Set other user properties as needed

        // Save user to the database (assuming UserRepository is available)
        userRepository.save(newUser);

        // Return success response
        return ResponseEntity.ok("User registered successfully");
    }
}

