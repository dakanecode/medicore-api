package com.hospital.hospitalapi.service;

import com.hospital.hospitalapi.dto.AppointmentDTO;
import com.hospital.hospitalapi.entity.Appointment;
import com.hospital.hospitalapi.entity.Doctor;
import com.hospital.hospitalapi.entity.Patient;
import com.hospital.hospitalapi.exception.ResourceNotFoundException;
import com.hospital.hospitalapi.mapper.AppointmentMapper;
import com.hospital.hospitalapi.repository.AppointmentRepository;
import com.hospital.hospitalapi.repository.DoctorRepository;
import com.hospital.hospitalapi.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentMapper appointmentMapper;

  //  createAppointment
    public AppointmentDTO createAppointment(AppointmentDTO dto){
        // fetch doctor from database
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + dto.getDoctorId()));

        // fetch patient from database
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + dto.getPatientId()));
        Appointment appointment = Appointment.builder()
                .appDate(dto.getAppDate())
                .appTime(dto.getAppTime())
                .reason(dto.getReason())
                .docNotes(dto.getDocNotes())
                .appStatus(dto.getAppStatus())
                .doctor(doctor)
                .patient(patient)
                .build();

       return appointmentMapper.toDTO(appointmentRepository.save(appointment));
    }

    // getAllAppointments
        // returns a list of appointments
    public List<AppointmentDTO> getAllAppointments(){
        return appointmentRepository.findAll()
                .stream()
                .map(appointmentMapper::toDTO)
                .toList();
    }

    // getAppointmentById....
    public AppointmentDTO getAppointmentById(Long id){
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with: " + id + " not found"));
        return appointmentMapper.toDTO(appointment);
    }
    // updateAppointment
    public AppointmentDTO updateAppointment(Long id, AppointmentDTO dto){
        // fetch doc by id, If a patient wants to change doctor, we can easily do it
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with: " + id + " not found"));

        // // fetch appointment by id
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with: " + id + " is not found"));
        appointment.setAppDate(dto.getAppDate());
        appointment.setAppTime(dto.getAppTime());
        appointment.setReason(dto.getReason());
        appointment.setDocNotes(dto.getDocNotes());
        appointment.setAppStatus(dto.getAppStatus());
        appointment.setDoctor(doctor);

        return appointmentMapper.toDTO(appointmentRepository.save(appointment));
    }

    // cancelAppointment
    public AppointmentDTO cancelAppointment(Long id){
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with: " + id + " not found"));
        appointment.setAppStatus(Appointment.AppointmentStatus.CANCELLED);

        return appointmentMapper.toDTO(appointmentRepository.save(appointment));

    }
    // getAppointmentsByDoctor... find doc by id
    public List<AppointmentDTO> getAppointmentsByDoctor(Long id){
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with: " + id + " not found"));

        // list of appointments
        List<Appointment> appointments = appointmentRepository.findByDoctor(doctor);
        return appointments.stream()
                .map(appointmentMapper::toDTO)
                .toList();
    }
//    getAppointmentsByPatient
    public List<AppointmentDTO> getAppointmentsByPatient(Long id){
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with id: " + id + " not found"));

        List<Appointment> appointments = appointmentRepository.findByPatient(patient);
        return  appointments.stream()
                .map(appointmentMapper::toDTO)
                .toList();
    }



}
