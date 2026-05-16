package com.hospital.hospitalapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorFilterRequest {
    private String specialty;
    private String department;
    private Boolean available;
    private Integer minYears;
    private String keyword;
}
