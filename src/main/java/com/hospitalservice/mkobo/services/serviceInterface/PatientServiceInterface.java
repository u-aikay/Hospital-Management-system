package com.hospitalservice.mkobo.services.serviceInterface;

import com.hospitalservice.mkobo.entities.Patient;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface PatientServiceInterface {
    ResponseEntity<String> addPatient(Patient patientObject);

    ResponseEntity<byte[]> downloadProfile(Long id, String token);

    ResponseEntity<List<Patient>> getPatientsByAge(int age);

    ResponseEntity<String> deletePatientByAge(int age, String token);

    ResponseEntity<String> deletePatientByAgeRange(int startAge,int stopAge, String token);
}
