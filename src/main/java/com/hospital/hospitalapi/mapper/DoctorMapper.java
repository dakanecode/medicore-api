package com.hospital.hospitalapi.mapper;

import com.hospital.hospitalapi.dto.DoctorDTO;
import com.hospital.hospitalapi.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DoctorMapper {
    DoctorDTO toDTO(Doctor doctor); // Entity -> dto
    Doctor toEntity(DoctorDTO doctorDTO); // dto -> entity
    void updateEntityFromDTO(DoctorDTO doctorDTO, @MappingTarget Doctor doctor);
}
