package com.hospital.hospitalapi.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Date is required")
    @Column(nullable = false)
    private String appDate;

    @NotBlank(message = "appointment time is required")
    @Column(nullable = false)
    private String appTime;

    @NotBlank(message = "Reason is required")
    @Column(nullable = false)
    private String reason;

    private  String docNotes;

    @NotNull(message = "status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus appStatus;

    public enum AppointmentStatus {
        SCHEDULED,
        COMPLETED,
        CANCELLED
    }

    @NotNull(message = "Doctor is required")
    @ManyToOne
    @JoinColumn(name ="doctor_id")
    private Doctor doctor;

    @NotNull(message = "Patient is required")
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
