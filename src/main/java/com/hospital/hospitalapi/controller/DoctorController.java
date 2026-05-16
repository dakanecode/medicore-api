package com.hospital.hospitalapi.controller;

import com.hospital.hospitalapi.dto.DoctorDTO;

import com.hospital.hospitalapi.dto.DoctorFilterRequest;
import com.hospital.hospitalapi.dto.common.PagedResponse;
import com.hospital.hospitalapi.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    //@GetMapping
    //public ResponseEntity<List<DoctorDTO>> getAllDoctor(){
       // return ResponseEntity.ok(doctorService.getAllDoctor());
    //}

    // GET one doctor by ID
    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id){
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }
    // getMapping
    @GetMapping
    public ResponseEntity<PagedResponse<DoctorDTO>> getDoctors(
            @PageableDefault(size = 10, sort = "lastName") Pageable pageable,
            @RequestParam(required = false) String speciality,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) int minYears,
            @RequestParam(required = false) String keyword){
        DoctorFilterRequest filter = new DoctorFilterRequest(
                speciality, department, available, minYears, keyword
        );
        return ResponseEntity.ok(doctorService.getDoctorsPaged(filter, pageable));
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
