package com.hospital.hospitalapi.controller;

import com.hospital.hospitalapi.dto.DoctorDTO;

import com.hospital.hospitalapi.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    // create a new doctor
    @PostMapping
    public ResponseEntity<DoctorDTO> createDoctor(@Valid @RequestBody DoctorDTO dto){
        return new ResponseEntity<>(doctorService.createDoctor(dto), HttpStatus.CREATED);
    }

    // GET ALL doctors
    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctor(){
        return ResponseEntity.ok(doctorService.getAllDoctor());
    }

    // GET one doctor by ID
    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id){
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }
    // by specialty
    @GetMapping("/specialty/{specialty}")
    public ResponseEntity<List<DoctorDTO>> getDoctorsBySpecialty(@PathVariable String specialty){
        return ResponseEntity.ok(doctorService.getDoctorsBySpecialty(specialty));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorDTO dto){
        return ResponseEntity.ok(doctorService.updateDoctor(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id){
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
}
