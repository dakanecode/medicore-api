package com.hospital.hospitalapi.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDTO {

    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone number is required")
    private  String phoneNumber;

    @NotBlank(message = "Specialty is required")
    private String specialty;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    @NotNull(message = "Years of experience is required")
    private Integer yearsExperience;

    @NotNull(message = "Salary is required")
    private Double salary;

    private boolean available;

}
