package com.hospital.hospitalapi.service;

import com.hospital.hospitalapi.dto.DoctorDTO;
import com.hospital.hospitalapi.entity.Doctor;
import com.hospital.hospitalapi.exception.ResourceNotFoundException;
import com.hospital.hospitalapi.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;

    // create
    public DoctorDTO createDoctor(DoctorDTO dto){
        if(doctorRepository.existsByEmail(dto.getEmail())){
            throw new RuntimeException("Email already registered: " + dto.getEmail());
        }
        if(doctorRepository.existsByLicenseNumber(dto.getLicenseNumber())){
            throw new RuntimeException("License number already registered: " + dto.getLicenseNumber());
        }
        Doctor doctor = Doctor.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .specialty(dto.getSpecialty())
                .department(dto.getDepartment())
                .licenseNumber(dto.getLicenseNumber())
                .yearsExperience(dto.getYearsExperience())
                .salary(dto.getSalary())
                .available(dto.isAvailable())
                .build();
        Doctor saved = doctorRepository.save(doctor);
        return mapToDTO(saved);
    }

    // Get all doctors
    public List<DoctorDTO> getAllDoctor(){
        return doctorRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    // get one by id
    public DoctorDTO getDoctorById(Long id){
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with " + id + " not found"));
        return mapToDTO(doctor);
    }

    // get Doctors By Specialty
    public List<DoctorDTO> getDoctorsBySpecialty(String specialty){
        return doctorRepository.findBySpecialty(specialty)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    // update
    public DoctorDTO updateDoctor(Long id, DoctorDTO dto){
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with " + id + " not found"));

        doctor.setFirstName(dto.getFirstName());
        doctor.setLastName(dto.getLastName());
        doctor.setEmail(dto.getEmail());
        doctor.setPhoneNumber(dto.getPhoneNumber());
        doctor.setSpecialty(dto.getSpecialty());
        doctor.setDepartment(dto.getDepartment());
        doctor.setLicenseNumber(dto.getLicenseNumber());
        doctor.setYearsExperience(dto.getYearsExperience());
        doctor.setSalary(dto.getSalary());
        doctor.setAvailable(dto.isAvailable());

        return mapToDTO(doctorRepository.save(doctor));
    }
    // delete
    public void deleteDoctor(Long id){
        doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with " + id + " not found"));
        doctorRepository.deleteById(id);
    }

    // Helper methods:
    private DoctorDTO mapToDTO(Doctor doctor){
        return DoctorDTO.builder()
                .id(doctor.getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .email(doctor.getEmail())
                .phoneNumber(doctor.getPhoneNumber())
                .specialty(doctor.getSpecialty())
                .department(doctor.getDepartment())
                .licenseNumber(doctor.getLicenseNumber())
                .yearsExperience(doctor.getYearsExperience())
                .salary(doctor.getSalary())
                .available(doctor.isAvailable())
                .build();

    }

}
