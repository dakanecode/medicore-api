package com.hospital.service;

import com.hospital.hospitalapi.dto.DoctorDTO;
import com.hospital.hospitalapi.entity.Doctor;
import com.hospital.hospitalapi.exception.ResourceNotFoundException;
import com.hospital.hospitalapi.repository.DoctorRepository;
import com.hospital.hospitalapi.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorService doctorService;

    private Doctor doctor;
    private DoctorDTO doctorDTO;

    @BeforeEach
    void setUp() {
        doctor = Doctor.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .phoneNumber("0734566578")
                .specialty("Cardiologist")
                .department("Cardiology")
                .licenseNumber("sr4445")
                .yearsExperience(5)
                .salary(12345.88)
                .available(true)
                .build();

        doctorDTO = DoctorDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .phoneNumber("0734566578")
                .specialty("Cardiologist")
                .department("Cardiology")
                .licenseNumber("sr4445")
                .yearsExperience(5)
                .salary(12345.88)
                .available(true)
                .build();
    }

    // ====== TEST 1 =====
    @Test
    @DisplayName("Should create doctor successfully")
    void shouldCreateDoctorSuccessfully() {

        // ARRANGE
        when(doctorRepository.existsByEmail(anyString())).thenReturn(false);
        when(doctorRepository.existsByLicenseNumber(anyString())).thenReturn(false);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        // ACT
        DoctorDTO result = doctorService.createDoctor(doctorDTO);

        // ASSERT
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("john@gmail.com", result.getEmail());
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    // ==== TEST 2 ====
    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        // ARRANGE
        when(doctorRepository.existsByEmail(anyString())).thenReturn(true);

        // ACT + ASSERT
        assertThrows(RuntimeException.class, () -> {
            doctorService.createDoctor(doctorDTO);
        });
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    // ==== TEST 3 ====
    @Test
    @DisplayName("Should throw exception when license number already exists")
    void shouldThrowExceptionWhenLicenseAlreadyExists() {

        // ARRANGE
        when(doctorRepository.existsByEmail(anyString())).thenReturn(false);
        when(doctorRepository.existsByLicenseNumber(anyString())).thenReturn(true);

        // ACT + ASSERT
        assertThrows(RuntimeException.class, () -> {
            doctorService.createDoctor(doctorDTO);
        });
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    // ==== TEST 4 ====
    @Test
    @DisplayName("Should return doctor when found by id")
    void shouldReturnDoctorWhenFoundById() {

        // ARRANGE
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        // ACT
        DoctorDTO result = doctorService.getDoctorById(1L);

        // ASSERT
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
    }

    // ==== TEST 5 ====
    @Test
    @DisplayName("Should throw ResourceNotFoundException when doctor not found")
    void shouldThrowExceptionWhenDoctorNotFound() {

        // ARRANGE
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            doctorService.getDoctorById(999L);
        });
    }

    // ==== TEST 6 ====
    @Test
    @DisplayName("Should return list of all doctors")
    void shouldReturnAllDoctors() {

        // ARRANGE
        List<Doctor> doctors = List.of(doctor);
        when(doctorRepository.findAll()).thenReturn(doctors);

        // ACT
        List<DoctorDTO> result = doctorService.getAllDoctor();

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(doctorRepository, times(1)).findAll();
    }

    // ==== TEST 7 ====
    @Test
    @DisplayName("Should update doctor successfully")
    void shouldUpdateDoctorSuccessfully() {

        // ARRANGE
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        // ACT
        DoctorDTO result = doctorService.updateDoctor(1L, doctorDTO);

        // ASSERT
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    // ==== TEST 8 ====
    @Test
    @DisplayName("Should throw exception when updating non-existent doctor")
    void shouldThrowExceptionWhenUpdatingNonExistentDoctor() {

        // ARRANGE
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            doctorService.updateDoctor(999L, doctorDTO);
        });
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    // ==== TEST 9 ====
    @Test
    @DisplayName("Should delete doctor successfully")
    void shouldDeleteDoctorSuccessfully() {

        // ARRANGE
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        doNothing().when(doctorRepository).deleteById(1L);

        // ACT
        doctorService.deleteDoctor(1L);

        // ASSERT
        verify(doctorRepository, times(1)).deleteById(1L);
    }

    // ==== TEST 10 ====
    @Test
    @DisplayName("Should throw exception when deleting non-existent doctor")
    void shouldThrowExceptionWhenDeletingNonExistentDoctor() {

        // ARRANGE
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            doctorService.deleteDoctor(999L);
        });
        verify(doctorRepository, never()).deleteById(anyLong());
    }
}