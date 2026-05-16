package com.hospital.hospitalapi.mapper;

import com.hospital.hospitalapi.dto.PatientDTO;
import com.hospital.hospitalapi.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PatientMapper {
    PatientDTO toDTO(Patient patient);
    Patient toEntity(PatientDTO patientDTO);
    void updatePatientFromDTO(PatientDTO dto, @MappingTarget Patient patient);
}
