package com.hospital.service;

import com.hospital.hospitalapi.dto.PatientDTO;
import com.hospital.hospitalapi.entity.Patient;
import com.hospital.hospitalapi.exception.ResourceNotFoundException;
import com.hospital.hospitalapi.repository.PatientRepository;
import com.hospital.hospitalapi.service.PatientService;
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
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;
    private PatientDTO patientDTO;

    @BeforeEach
    void setUp() {
        // This runs before EVERY test
        // Set up common test data here

        patient = Patient.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .phoneNumber("0712345678")
                .address("123 Nairobi")
                .dateOfBirth("1990-01-15")
                .gender(Patient.Gender.MALE)
                .build();

        patientDTO = PatientDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .phoneNumber("0712345678")
                .address("123 Nairobi")
                .dateOfBirth("1990-01-15")
                .gender("MALE")
                .build();
    }

    // ===== TEST 1 =====
    @Test
    @DisplayName("Should create patient successfully")
    void shouldCreatePatientSuccessfully() {

        // ARRANGE
        when(patientRepository.existsByEmail(anyString())).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // ACT
        PatientDTO result = patientService.createPatient(patientDTO);

        // ASSERT
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("john@email.com", result.getEmail());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    // ===== TEST 2 =====
    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        // ARRANGE
        when(patientRepository.existsByEmail(anyString())).thenReturn(true);

        // ACT + ASSERT
        assertThrows(RuntimeException.class, () -> {
            patientService.createPatient(patientDTO);
        });

        // Verify save was NEVER called
        verify(patientRepository, never()).save(any(Patient.class));
    }

    // ===== TEST 3 =====
    @Test
    @DisplayName("Should return patient when found by id")
    void shouldReturnPatientWhenFoundById() {

        // ARRANGE
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        // ACT
        PatientDTO result = patientService.getPatientById(1L);

        // ASSERT
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
    }

    // ===== TEST 4 =====
    @Test
    @DisplayName("Should throw ResourceNotFoundException when patient not found")
    void shouldThrowExceptionWhenPatientNotFound() {

        // ARRANGE
        when(patientRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            patientService.getPatientById(999L);
        });
    }

    // ===== TEST 5 =====
    @Test
    @DisplayName("Should delete patient successfully")
    void shouldDeletePatientSuccessfully() {

        // ARRANGE
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        doNothing().when(patientRepository).deleteById(1L);

        // ACT
        patientService.deletePatient(1L);

        // ASSERT
        verify(patientRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should return a list of patients")
    void shouldReturnAllPatients(){
        // ARRANGE
        List<Patient> patients = List.of(patient);
        when(patientRepository.findAll()).thenReturn(patients);

        // ACT
        List<PatientDTO> result = patientService.getAllPatients();

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should update patients successfully")
    void shouldUpdatePatientSuccessfully(){

        // ARRANGE
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // ACT
        PatientDTO result = patientService.updatePatient(1L, patientDTO);

        // ASSERT
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }
    @Test
    @DisplayName("Should throw exception when updating non-existent patient")
    void shouldThrowExceptionWhenUpdatingNonExistentPatient() {

        // ARRANGE
        when(patientRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            patientService.updatePatient(999L, patientDTO);
        });

        verify(patientRepository, never()).save(any(Patient.class));
    }
}