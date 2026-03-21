package com.hospital.service;

import com.hospital.hospitalapi.dto.AuthResponse;
import com.hospital.hospitalapi.dto.LoginRequest;
import com.hospital.hospitalapi.dto.RegisterRequest;
import com.hospital.hospitalapi.entity.User;
import com.hospital.hospitalapi.repository.UserRepository;
import com.hospital.hospitalapi.security.JwtService;
import com.hospital.hospitalapi.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private User user;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp(){
        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Smith")
                .email("john@gmail.com")
                .password("1234hy")
                .role(User.Role.ADMIN)
                .build();
        registerRequest = RegisterRequest.builder()
                .firstName("John")
                .lastName("Smith")
                .email("john@gmail.com")
                .password("1234hy")
                .role(User.Role.ADMIN)
                .build();
        loginRequest = LoginRequest.builder()
                .email("john@gmail.com")
                .password("1234hy")
                .build();
    }
     // === TEST 1 ===
    @Test
    @DisplayName("Should register a user successfully")
    void shouldRegisterUserSuccessfully(){
        // ARRANGE
        when(userRepository.existsByEmail(anyString()))
                .thenReturn(false);
        when(passwordEncoder.encode(anyString()))
                .thenReturn("1234hy");
        when(userRepository.save(any(User.class)))
                .thenReturn(user);
        // 4. Token generation
        when(jwtService.generateToken(any(User.class)))
                .thenReturn("mockedToken");
        // ACT
        AuthResponse result  = authService.register(registerRequest);
        // assert
        assertNotNull(result);
        assertEquals("mockedToken", result.getToken());
        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateToken(any(User.class));

    }

    @Test
    @DisplayName("should throw exception when email already exist")
    void shouldThrowExceptionWhenEmailAlreadyExists(){
        // ARRANGE
        when(userRepository.existsByEmail(anyString()))
                .thenReturn(true);

        // act + assert
        assertThrows(RuntimeException.class, () -> {
            authService.register(registerRequest);
        });

        // User should NEVER be saved!
        verify(userRepository, never()).save(any(User.class));
        // Token should NEVER be generated!
        verify(jwtService, never()).generateToken(any(User.class));
    }

    @Test
    @DisplayName("Should login user successfully")
    void shouldLoginUserSuccessfully(){
        // ARRANGE
        // authenticate() is void — use doNothing()!
        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user));

        when(jwtService.generateToken(any(User.class)))
                .thenReturn("mockedToken");
        // ACT
        AuthResponse result = authService.login(loginRequest);

        // assert
        assertNotNull(result);
        assertEquals("mockedToken", result.getToken());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(jwtService, times(1)).generateToken(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found during login")
    void shouldThrowExceptionWhenUserNotFoundDuringLogin(){
        // ARRANGE
        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);

        // User not found!
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        // Token should NEVER be generated!
        verify(jwtService, never()).generateToken(any(User.class));
    }

}
