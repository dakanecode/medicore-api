package com.hospital.hospitalapi.specification;

import com.hospital.hospitalapi.entity.Doctor;
import org.springframework.data.jpa.domain.Specification;

public class DoctorSpecification {
    // filter by findByLicenseNumber
    // findBySpecialty
    public static Specification<Doctor> hasSpecialty(String specialty) {
        return (root, query, cb) -> {
            if(specialty == null || specialty.isBlank()) return null;
            return cb.equal(root.get("specialty"), specialty);
        };
    }
    // filter by department
    public static Specification<Doctor> hasDepartment(String department) {
        return(root, query, cb) -> {
            if(department == null || department.isBlank()) return null;
            return  cb.equal(root.get("department"), department);
        };
    }
    // filter by availability
    public static Specification<Doctor> isAvailable(Boolean available) {
        return(root, query, cb) -> {
            if(available == null) return null;
            return cb.equal(root.get("available"), true);
        };
    }
    // filter by minimum year of experience
    public static Specification<Doctor> hasMinExperience(Integer minYears) {
        return (root, query,cb) -> {
            if(minYears == null) return null;
            return cb.greaterThanOrEqualTo(root.get("yearsExperience"), minYears);
        };
    }
    // search by first or last name(case sensitive partial match)
    public static Specification<Doctor> nameContains(String keyword) {
        return(root, query, cb) -> {
            if(keyword == null || keyword.isBlank()) return null;
            String pattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("firstName")), pattern),
                    cb.like(cb.lower(root.get("lastName")), pattern)
            );
        };
    }
    /*

    */
     /*
         public static Specification<Doctor> isAvailable(Boolean available) {
        return (root, query, cb) -> {
            if (available == null) return null;
            return cb.equal(root.get("available"), available);
        };
    }
     */
}
