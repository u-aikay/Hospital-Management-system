package com.hospitalservice.mkobo.services.serviceImpl;

import com.hospitalservice.mkobo.entities.Patient;
import com.hospitalservice.mkobo.entities.Staff;
import com.hospitalservice.mkobo.repositories.PatientRepository;
import com.hospitalservice.mkobo.services.serviceInterface.PatientServiceInterface;
import com.hospitalservice.mkobo.utility.Reusable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientServiceInterface {

    private final PatientRepository patientRepository;
    private final StaffServiceImpl staffServiceInterface;
    private final Reusable reusable;

    @Override
    public ResponseEntity<String> addPatient(Patient patientObject) {

        try{

            patientRepository.save(patientObject);
            return new ResponseEntity<>("Patient successfully registered", HttpStatus.CREATED);
        }catch (Exception e){

            System.out.println("===> PATIENT REGISTRATION CAUGHT AN EXCEPTION");
            e.printStackTrace();
        }

        return new ResponseEntity<>("Patient registration failed", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<byte[]> downloadProfile(Long id, String token) {

        //validate staff profile to download patient profile
        String staffEmail = Objects.requireNonNull(reusable.fetchTokenUuidAndEmail(token).getBody()).getEmail();
        Optional<Staff> registeredStaff = staffServiceInterface.findByEmail(staffEmail);
        if (!reusable.validateTokenAndUuid(token, registeredStaff.get().getUuid()))
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        //start initiating patient profile download
        Optional<Patient> registeredPatient = patientRepository.findById(id);

        if(registeredPatient.isPresent()){

            try{
                String cid = String.valueOf(registeredPatient.get().getId());
                String fn = registeredPatient.get().getFirstName().toUpperCase();
                String ln = registeredPatient.get().getLastName().toUpperCase();
                String ag = String.valueOf(registeredPatient.get().getAge());
                String lvd = String.valueOf(registeredPatient.get().getLast_visit_date());

                StringBuilder csvContent = new StringBuilder();
                csvContent.append("ID,FIRST-NAME,LAST-NAME,AGE,LAST-VISIT-DATE\n");
                csvContent.append(cid + "," + fn + "," + ln+ "," + ag + "," + lvd);


                byte[] csvBytes = csvContent.toString().getBytes();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", fn+"_patient_profile.csv");
                return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);

            }catch(Exception e){

                System.out.println("===> PATIENT PROFILE DOWNLOAD CAUGHT AN EXCEPTION");
                e.printStackTrace();
            }
        }

        return new ResponseEntity<>(null, null, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<Patient>> getPatientsByAge(int age) {
        List<Patient> patientList = patientRepository.findByAgeGreaterThanEqual(age);

        return new ResponseEntity<>(patientList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deletePatientByAge(int age, String token) {
        //validate staff data confirm uuid in token
        String staffEmail = Objects.requireNonNull(reusable.fetchTokenUuidAndEmail(token).getBody()).getEmail();
        Optional<Staff> registeredStaff = staffServiceInterface.findByEmail(staffEmail);
        if (!reusable.validateTokenAndUuid(token, registeredStaff.get().getUuid()))
            return new ResponseEntity<>("Staff data mismatch", HttpStatus.BAD_REQUEST);
        if (!reusable.validateHODRole(registeredStaff.get()))
            return new ResponseEntity<>("unauthorised request", HttpStatus.FORBIDDEN);

        int deletedDataCount = patientRepository.deleteByAgeEqual(age);
        return new ResponseEntity<>(deletedDataCount + " patient data deleted", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deletePatientByAgeRange(int startAge, int stopAge, String token) {
        //validate staff data confirm uuid in token
        String staffEmail = Objects.requireNonNull(reusable.fetchTokenUuidAndEmail(token).getBody()).getEmail();
        Optional<Staff> registeredStaff = staffServiceInterface.findByEmail(staffEmail);
        if (!reusable.validateTokenAndUuid(token, registeredStaff.get().getUuid()))
            return new ResponseEntity<>("Staff data mismatch", HttpStatus.BAD_REQUEST);
        if (!reusable.validateHODRole(registeredStaff.get()))
            return new ResponseEntity<>("unauthorised request", HttpStatus.FORBIDDEN);

        int deletedDataCount = patientRepository.deleteByAgeRange(startAge,startAge);
        return new ResponseEntity<>(deletedDataCount + " patient data deleted", HttpStatus.OK);
    }
}
