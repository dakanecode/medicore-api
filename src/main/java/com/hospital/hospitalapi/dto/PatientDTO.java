package com.hospital.hospitalapi.dto;
import jakarta.validation.constraints.*;
import lombok.*;

import javax.swing.plaf.BorderUIResource;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDTO {
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message ="Email is required")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private  String address;

    @NotBlank(message = "Date of birth is required")
    private String dateOfBirth;

    private String gender;
}
