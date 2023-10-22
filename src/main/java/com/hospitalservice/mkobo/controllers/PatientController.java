package com.hospitalservice.mkobo.controllers;

import com.hospitalservice.mkobo.entities.Patient;
import com.hospitalservice.mkobo.services.serviceInterface.PatientServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping(path = "/api/v1/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientServiceInterface patientServiceInterface;

    //ADD PATIENT
    @PostMapping("add")
    public ResponseEntity<String> addPatient(@RequestBody Patient patientObject){

        System.out.println("Patient registration started ==>");
        return patientServiceInterface.addPatient(patientObject);
    }


    //READ SINGLE PATIENT PROFILE AND DOWNLOAD PROFILE AS CSV
    @GetMapping("/download_profile/{id}")
    public ResponseEntity<byte[]> downloadProfile(@PathVariable Long id, @RequestHeader("Authorization") String token) {

        System.out.println("Initiated patient profile download ==>");
        return patientServiceInterface.downloadProfile(id, token);
    }


    //READ ALL PATIENT PROFILE
    //READ ALL PATIENT PROFILE ON SORT PARAMETERS(AGE > SPECIFIED NUMBER)
    @GetMapping("/get_all/{age}")
    public ResponseEntity<List<Patient>> getAllPatientByAge(@PathVariable int age){

        System.out.println("Fetch patient profile by age method initiated ==>");
        return patientServiceInterface.getPatientsByAge(age);
    }


    //DELETE ALL PATIENT PROFILE ON SORT PARAMETERS(AGE == SPECIFIC NUMBER)
    @DeleteMapping("/delete/{age}")
    public ResponseEntity<String> deletePatientByAge(@PathVariable int age, @RequestHeader("Authorization") String token){

        System.out.println("Delete patient profile by age method initiated ==>");
        return patientServiceInterface.deletePatientByAge(age, token);
    }

    @DeleteMapping("/delete/{startAge}/{stopAge}")
    public ResponseEntity<String> deletePatientByAgeRange(@PathVariable int startAge,@PathVariable int stopAge, @RequestHeader("Authorization") String token){

        System.out.println("Delete patient profile by age method initiated ==>");
        return patientServiceInterface.deletePatientByAgeRange(startAge, stopAge, token);
    }
}
