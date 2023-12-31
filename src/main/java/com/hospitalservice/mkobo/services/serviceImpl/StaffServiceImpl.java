package com.hospitalservice.mkobo.services.serviceImpl;

import com.hospitalservice.mkobo.entities.Staff;
import com.hospitalservice.mkobo.repositories.StaffRepository;
import com.hospitalservice.mkobo.services.serviceInterface.StaffServiceInterface;
import com.hospitalservice.mkobo.utility.Reusable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffServiceInterface {


    private final StaffRepository staffRepository;
    private final Reusable reusable;


    @Override
    public ResponseEntity<String> registerStaff(Staff staffObject, String token) {

        System.out.println("got here 1");
        //confirm uuid in token
        Optional<Staff> registeredStaff = staffRepository.findByEmail(staffObject.getEmail());
        if (!reusable.validateTokenAndUuid(token, registeredStaff.get().getUuid()))
            return new ResponseEntity<>("Staff data mismatch", HttpStatus.BAD_REQUEST);
        if (!reusable.validateHODRole(registeredStaff.get()))
            return new ResponseEntity<>("unauthorised request", HttpStatus.FORBIDDEN);

        System.out.println("got here 2");
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            String autoGeneratedUUID = Reusable.generateUUID();

            staffObject.setRegistration_date(new Date());
            staffObject.setUuid(autoGeneratedUUID);
            staffObject.setPassword(encoder.encode(staffObject.getPassword()));
            staffRepository.save(staffObject);
            return new ResponseEntity<>("Staff successfully registered", HttpStatus.CREATED);
        } catch (Exception e) {

            System.out.println("===> STAFF REGISTRATION CAUGHT AN EXCEPTION");
            e.printStackTrace();
        }

        return new ResponseEntity<>("Staff registration failed", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<String> updateProfile(Staff staffObject, String token) {
        Optional<Staff> registeredStaff = staffRepository.findByEmail(staffObject.getEmail());

        if (!reusable.validateTokenAndUuid(token, registeredStaff.get().getUuid()))
            return new ResponseEntity<>("Staff data mismatch", HttpStatus.BAD_REQUEST);

        registeredStaff.get().setFirstName(staffObject.getFirstName());
        registeredStaff.get().setLastName(staffObject.getLastName());
        staffRepository.save(registeredStaff.get());

        return new ResponseEntity<>("Staff profile successfully updated", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> updateRole(Staff staffObject, String token) {

        //confirm uuid in token
        Optional<Staff> registeredStaff = staffRepository.findByEmail(staffObject.getEmail());
        if (!reusable.validateTokenAndUuid(token, registeredStaff.get().getUuid()))
            return new ResponseEntity<>("Staff data mismatch", HttpStatus.BAD_REQUEST);
        if (!reusable.validateHODRole(registeredStaff.get()))
            return new ResponseEntity<>("unauthorised request", HttpStatus.FORBIDDEN);


        registeredStaff.get().setRole(staffObject.getRole());
        staffRepository.save(registeredStaff.get());

        return new ResponseEntity<>("Staff role successfully updated", HttpStatus.CREATED);
    }


    @Override
    public Optional<Staff> findByEmail(String email) {
        return staffRepository.findByEmail(email);
    }
}
