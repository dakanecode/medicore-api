package com.hospital.hospitalapi.mapper;

import com.hospital.hospitalapi.dto.AppointmentDTO;
import com.hospital.hospitalapi.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AppointmentMapper {
    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "patient.id", target = "patientId")

    @Mapping(target = "doctorName",
            expression = "java(appointment.getDoctor().getFirstName() + \" \" + appointment.getDoctor().getLastName())")
    @Mapping(target = "patientName",
            expression = "java(appointment.getPatient().getFirstName() + \" \" + appointment.getPatient().getLastName())")
    AppointmentDTO toDTO(Appointment appointment);

}
