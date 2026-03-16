package com.hospital.hospitalapi.repository;

import com.hospital.hospitalapi.entity.Appointment;
import com.hospital.hospitalapi.entity.Doctor;
import com.hospital.hospitalapi.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorAndAppStatus(Doctor doctor, Appointment.AppointmentStatus status);
    List<Appointment> findByDoctor(Doctor doctor);
    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByAppDate(String appDate);


}
