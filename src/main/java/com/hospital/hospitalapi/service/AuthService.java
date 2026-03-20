package com.hospital.hospitalapi.service;

import com.hospital.hospitalapi.dto.AuthResponse;
import com.hospital.hospitalapi.dto.LoginRequest;
import com.hospital.hospitalapi.dto.RegisterRequest;
import com.hospital.hospitalapi.entity.User;
import com.hospital.hospitalapi.repository.UserRepository;
import com.hospital.hospitalapi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // REGISTER
    public AuthResponse register(RegisterRequest request) {

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered: " + request.getEmail());
        }

        // Build user object
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        // Save user to database
        userRepository.save(user);

        // Generate JWT token
        String token = jwtService.generateToken(user);

        // Return token
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    // LOGIN
    public AuthResponse login(LoginRequest request) {

        // Authenticate — checks email and password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Load user from database
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate JWT token
        String token = jwtService.generateToken(user);

        // Return token
        return AuthResponse.builder()
                .token(token)
                .build();
    }
}