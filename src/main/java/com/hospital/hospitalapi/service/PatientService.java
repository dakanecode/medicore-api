package com.hospital.hospitalapi.service;
import com.hospital.hospitalapi.dto.PatientDTO;
import com.hospital.hospitalapi.entity.Patient;
import com.hospital.hospitalapi.exception.ResourceNotFoundException;
import com.hospital.hospitalapi.mapper.PatientMapper;
import com.hospital.hospitalapi.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    // CREATE
    public PatientDTO createPatient(PatientDTO dto){
        if(patientRepository.existsByEmail(dto.getEmail())){
            throw new RuntimeException("Email already registered: " + dto.getEmail());
        }
        Patient saved = patientRepository.save(patientMapper.toEntity(dto));
        return patientMapper.toDTO(saved);
    }
    // GET ALL
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toDTO)
                .collect(Collectors.toList());
    }
    // GET ONE
    public PatientDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        return patientMapper.toDTO(patient);
    }
    // UPDATE
    public PatientDTO updatePatient(Long id, PatientDTO dto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        patientMapper.updatePatientFromDTO(dto, patient);
        return patientMapper.toDTO(patientRepository.save(patient));
    }
    // DELETE
    public void deletePatient(Long id) {
        patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        patientRepository.deleteById(id);
    }
}
