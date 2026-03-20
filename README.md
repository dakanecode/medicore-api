# Medicore API 🏥

A RESTful API for managing hospital operations built with Java and Spring Boot.

## Tech Stack
- Java 21
- Spring Boot 3.4.3
- MySQL
- Spring Data JPA / Hibernate
- Lombok
- Maven

## Modules
- Patient Management
- Doctor Management  
- Appointment Booking

## API Endpoints

### Patients
- POST /api/v1/patients
- GET /api/v1/patients
- GET /api/v1/patients/{id}
- PUT /api/v1/patients/{id}
- DELETE /api/v1/patients/{id}

### Doctors
- POST /api/v1/doctors
- GET /api/v1/doctors
- GET /api/v1/doctors/{id}
- GET /api/v1/doctors/specialty/{specialty}
- PUT /api/v1/doctors/{id}
- DELETE /api/v1/doctors/{id}

### Appointments
- POST /api/v1/appointments
- GET /api/v1/appointments
- GET /api/v1/appointments/{id}
- PUT /api/v1/appointments/{id}
- PATCH /api/v1/appointments/{id}/cancel
- GET /api/v1/appointments/doctor/{doctorId}
- GET /api/v1/appointments/patient/{patientId}
