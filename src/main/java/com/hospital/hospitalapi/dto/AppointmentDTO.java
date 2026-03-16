package com.hospital.hospitalapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDTO {
    private Long id;

    @NotBlank(message = "Date is required")
    private String appDate;

    @NotBlank(message = "appointment time is required")
    private String appTime;

    @NotBlank(message = "Reason is required")
    private String reason;

    private  String docNotes;

    private String appStatus;

    @NotNull(message = "doctor id is required")
    private Long doctorId;

    @NotNull(message = "patient id is required")
    private Long patientId;

    private String doctorName;
    private String patientName;

}
