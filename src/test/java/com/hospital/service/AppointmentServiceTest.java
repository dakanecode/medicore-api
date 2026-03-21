package com.hospital.service;

import com.hospital.hospitalapi.dto.AppointmentDTO;
import com.hospital.hospitalapi.entity.Appointment;
import com.hospital.hospitalapi.entity.Doctor;
import com.hospital.hospitalapi.entity.Patient;
import com.hospital.hospitalapi.exception.ResourceNotFoundException;
import com.hospital.hospitalapi.repository.AppointmentRepository;
import com.hospital.hospitalapi.repository.DoctorRepository;
import com.hospital.hospitalapi.repository.PatientRepository;
import com.hospital.hospitalapi.service.AppointmentService;
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
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {
    // Appointment...
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Appointment appointment;
    private AppointmentDTO appointmentDTO;

    private Doctor doctor;
    private Patient patient;


    @BeforeEach
    void setUp(){
        doctor = Doctor.builder()
                .id(1L)
                .firstName("John")
                .lastName("Smith")
                .email("john@hospital.com")
                .licenseNumber("LIC123")
                .build();
        patient = Patient.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@example.com")
                .build();
        appointment = Appointment.builder()
                .id(1L)
                .appDate("2023-01-01")
                .appTime("09:00")
                .reason("Chest pain")
                .appStatus(Appointment.AppointmentStatus.SCHEDULED)
                .doctor(doctor)
                .patient(patient)
                .build();
        appointmentDTO = AppointmentDTO.builder()
                .appDate("2023-01-01")
                .appTime("09:00")
                .reason("Chest pain")
                .doctorName(doctor.getFirstName() + " " + doctor.getLastName())
                .patientName(patient.getFirstName() + " " + patient.getLastName())
                .doctorId(1L)
                .patientId(1L)
                .appStatus("SCHEDULED")
                .build();
    }
        // ===  TEST 1 ===
    @Test
    @DisplayName("Should create appointment successfully")
    void shouldCreateAppointmentSuccessfully(){

        // ARRANGE
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        // ACT
        AppointmentDTO result = appointmentService.createAppointment(appointmentDTO);

        // ASSERT
        assertNotNull(result);
        assertEquals("2023-01-01", result.getAppDate());
        assertEquals("09:00", result.getAppTime());
        assertEquals("John Smith", result.getDoctorName());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }
    // === TEST 2 ===
    @Test
    @DisplayName("Should throw an exception when Doctor not found")
    void shouldThrowExceptionWhenDoctorNotFound() {

        // ARRANGE
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(RuntimeException.class, () -> {
            appointmentService.createAppointment(appointmentDTO);
        });
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }
    // ====  TEST 3  ====
    @Test
    @DisplayName("Should throw an exception when patient not found")
    void shouldThrowExceptionWhenPatientNotFound() {

        // ARRANGE
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(RuntimeException.class, () -> {
            appointmentService.createAppointment(appointmentDTO);
        });
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }
     // === TEST 4 ===
    @Test
    @DisplayName("Should return appointment by id")
    void shouldReturnAppointmentById(){
        // ARRANGE
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        // ACT
        AppointmentDTO result = appointmentService.getAppointmentById(1L);
        // ASSERT
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Smith", result.getDoctorName());
    }

    // === TEST 5 ===
    @Test
    @DisplayName("Should throw ResourceNotFoundException when appointment not found")
    void shouldThrowExceptionWhenAppointmentNotFound(){
        // ARRANGE
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.getAppointmentById(999L);
        });
    }
    // === TEST 6 ===
    @Test
    @DisplayName("should return list of all appointments")
    void shouldReturnAllDoctor(){
        // ARRANGE
        List<Appointment> appointments = List.of(appointment);
        when(appointmentRepository.findAll()).thenReturn(appointments);

        // ACT
        List<AppointmentDTO> result = appointmentService.getAllAppointments();

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Smith", result.get(0).getDoctorName());
        verify(appointmentRepository, times(1)).findAll();
    }

    // ===  TEST 7 ===
    @Test
    @DisplayName("should return appointment by patient")
    void shouldReturnAppointmentByPatient(){
        List<Appointment> appointments = List.of(appointment);
        // ARRANGE
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findByPatient(patient)).thenReturn(appointments);

        // ACT
        List<AppointmentDTO> result = appointmentService.getAppointmentsByPatient(1L);
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Jane Doe", result.get(0).getPatientName());
        verify(appointmentRepository,times(1)).findByPatient(patient);
    }

    // === TEST 8 ===
    @Test
    @DisplayName("Should return appointement by doctor")
    void shouldReturnAppointmentByDoctor(){
        List<Appointment> appointments = List.of(appointment);
        // Arrange
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByDoctor(doctor)).thenReturn(appointments);

        // ACT
        List<AppointmentDTO> result = appointmentService.getAppointmentsByDoctor(1L);

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Smith", result.get(0).getDoctorName());
        verify(appointmentRepository, times(1)).findByDoctor(doctor);
    }

    // ===  TEST 9  ===
    @Test
    @DisplayName("Should update appointments successfully")
    void shouldUpdateAppointmentSuccessfully(){
        // ARRANGE
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        // ACT

        AppointmentDTO result = appointmentService.updateAppointment(1L, appointmentDTO);

        // ASSERT
        assertNotNull(result);
        assertEquals("Jane Doe", result.getPatientName());
        assertEquals("John Smith", result.getDoctorName());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));

    }
    // === TEST 10 ===
    @Test
    @DisplayName("Should throw exception when updating non-existent appointment")
    void shouldThrowExceptionWhenUpdatingNonExistentAppointment(){
        // ARRANGE
        when(doctorRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // ACT + ASSERT — should throw exception!
        assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.updateAppointment(999L, appointmentDTO);
        });

        // appointment never saved
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }


    // ==== TEST 11 ====
    @Test
    @DisplayName("Should delete appointment successfully")
    void shouldDeleteAppointmentSuccessfully() {

        // ARRANGE
        when(appointmentRepository.findById(1L))
                .thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class)))
                .thenReturn(appointment);


        // ACT
        AppointmentDTO result = appointmentService.cancelAppointment(1L);

        // ASSERT
        assertNotNull(result);
        verify(appointmentRepository, times(1)).findById(1L);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
        verify(appointmentRepository, never()).deleteById(anyLong());
    }

    // ==== TEST 12 ====
    @Test
    @DisplayName("Should throw exception when deleting non-existent appointment")
    void shouldThrowExceptionWhenDeletingNonExistentAppointment() {

        // ARRANGE
        when(appointmentRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.cancelAppointment(999L);
        });
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }


}

