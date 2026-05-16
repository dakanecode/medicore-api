package com.hospital.hospitalapi.service;

import com.hospital.hospitalapi.dto.DoctorDTO;
import com.hospital.hospitalapi.dto.DoctorFilterRequest;
import com.hospital.hospitalapi.dto.common.PagedResponse;
import com.hospital.hospitalapi.entity.Doctor;
import com.hospital.hospitalapi.exception.ResourceNotFoundException;
import com.hospital.hospitalapi.mapper.DoctorMapper;
import com.hospital.hospitalapi.repository.DoctorRepository;
import com.hospital.hospitalapi.specification.DoctorSpecification;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    // create
    public DoctorDTO createDoctor(DoctorDTO dto){
        if(doctorRepository.existsByEmail(dto.getEmail())){
            throw new RuntimeException("Email already registered: " + dto.getEmail());
        }
        if(doctorRepository.existsByLicenseNumber(dto.getLicenseNumber())){
            throw new RuntimeException("License number already registered: " + dto.getLicenseNumber());
        }
        Doctor saved = doctorRepository.save(doctorMapper.toEntity(dto));
        return doctorMapper.toDTO(saved);
    }

    // Get all doctors
//    public List<DoctorDTO> getAllDoctor(){
//        return doctorRepository.findAll()
//                .stream()
//                .map(doctorMapper::toDTO)
//                .toList();
//    }
    // get one by id
    public DoctorDTO getDoctorById(Long id){
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with " + id + " not found"));
        return doctorMapper.toDTO(doctor);
    }

    // get Doctors By Specialty
    public List<DoctorDTO> getDoctorsBySpecialty(String specialty){
        return doctorRepository.findBySpecialty(specialty)
                .stream()
                .map(doctorMapper::toDTO)
                .collect(Collectors.toList());
    }
    //
    public PagedResponse<DoctorDTO> getDoctorsPaged(DoctorFilterRequest filter, Pageable pageable){
        // combine all active filters using and()
        Specification<Doctor> spec = Specification
                .where(DoctorSpecification.hasSpecialty(filter.getSpecialty()))
                .and(DoctorSpecification.hasDepartment(filter.getDepartment()))
                .and(DoctorSpecification.isAvailable(filter.getAvailable()))
                .and(DoctorSpecification.hasMinExperience(filter.getMinYears()))
                .and(DoctorSpecification.nameContains(filter.getKeyword()));
        Page<Doctor> page = doctorRepository.findAll(spec,pageable);
        List<DoctorDTO> dtos = page.getContent()
                .stream()
                .map(doctorMapper::toDTO)
                .toList();
        return new PagedResponse<DoctorDTO>(
                dtos,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }





    // update
    public DoctorDTO updateDoctor(Long id, DoctorDTO dto){
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with " + id + " not found"));
    doctorMapper.updateEntityFromDTO(dto, doctor);
    return doctorMapper.toDTO(doctorRepository.save(doctor));
    }
    // delete
    public void deleteDoctor(Long id){
        doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with " + id + " not found"));
        doctorRepository.deleteById(id);
    }

}
