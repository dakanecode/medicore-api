package com.hospital.hospitalapi.service;
import com.hospital.hospitalapi.dto.PatientDTO;
import com.hospital.hospitalapi.entity.Patient;
import com.hospital.hospitalapi.exception.ResourceNotFoundException;
import com.hospital.hospitalapi.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class PatientService {

    private final PatientRepository patientRepository;

    // CREATE
    public PatientDTO createPatient(PatientDTO dto){
        if(patientRepository.existsByEmail(dto.getEmail())){
            throw new RuntimeException("Email already registered: " + dto.getEmail());
        }
        Patient patient = Patient.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .dateOfBirth(dto.getDateOfBirth())
                .gender(Patient.Gender.valueOf(dto.getGender()))
                .build();

        Patient saved = patientRepository.save(patient);
        return mapToDTO(saved);
    }
    // GET ALL
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    // GET ONE
    public PatientDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        return mapToDTO(patient);
    }
    // UPDATE
    public PatientDTO updatePatient(Long id, PatientDTO dto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setAddress(dto.getAddress());
        patient.setDateOfBirth(dto.getDateOfBirth());

        return mapToDTO(patientRepository.save(patient));
    }
    // DELETE
    public void deletePatient(Long id) {
        patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        patientRepository.deleteById(id);
    }

    // HELPER METHOD - converts Entity to DTO
    private PatientDTO mapToDTO(Patient patient) {
        return PatientDTO.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .email(patient.getEmail())
                .phoneNumber(patient.getPhoneNumber())
                .address(patient.getAddress())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender().name())
                .build();
    }
}
