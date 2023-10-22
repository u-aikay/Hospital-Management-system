package com.hospitalservice.mkobo.services.serviceInterface;

import com.hospitalservice.mkobo.entities.Staff;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

public interface StaffServiceInterface {
    ResponseEntity<String> registerStaff(Staff staffObject, String token);

    ResponseEntity<String> updateProfile(Staff staffObject, String token);

    ResponseEntity<String> updateRole(Staff staffObject, String token);

    Optional<Staff> findByEmail(String email);

}
