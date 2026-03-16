package com.hospital.hospitalapi.repository;

import com.hospital.hospitalapi.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    // search by license number and specialty
    Optional<Doctor> findByLicenseNumber(String licenseNumber);
    boolean existsByLicenseNumber(String licenseNumber);
    boolean existsByEmail(String email);

    List<Doctor> findBySpecialty(String specialty);


}
