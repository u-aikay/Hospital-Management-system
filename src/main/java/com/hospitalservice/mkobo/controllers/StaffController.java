package com.hospitalservice.mkobo.controllers;

import com.hospitalservice.mkobo.configs.securityConfigs.AuthenticationService;
import com.hospitalservice.mkobo.dtos.AuthenticationRequest;
import com.hospitalservice.mkobo.dtos.AuthenticationResponse;
import com.hospitalservice.mkobo.entities.Staff;
import com.hospitalservice.mkobo.services.serviceInterface.StaffServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/staff")
@RequiredArgsConstructor
public class StaffController {
//todo: setup security and use it on the endpoints

    private final StaffServiceInterface staffServiceInterface;
    private final AuthenticationService authenticationService;


    //REGISTER STAFF
    /**
        Staff registration can only be done by the HOD,
        A default HOD profile has been preloaded to the dataBase
        -User will need to login as the HOD
        -with the generated token, the endpoint to add staff can be called.
        -Login credential{email = aikay@ymail.com; password = 12345}
    */
    @PostMapping("/add_staff")
    public ResponseEntity<String> registerStaff(@RequestBody Staff staffObject, @RequestHeader("Authorization") String token) {

        System.out.println("Staff registration started ==>");
        return staffServiceInterface.registerStaff(staffObject, token);
    }

    //AUTHENTICATE STAFF
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {

        System.out.println("Staff authentication started ==>");
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }


    //UPDATE STAFF PROFILE
    @PutMapping("/update/profile")
    public ResponseEntity<String> updateProfile(@RequestBody Staff staffObject, @RequestHeader("Authorization") String token) {

        System.out.println("Staff profile update started ==>");
        return staffServiceInterface.updateProfile(staffObject, token);
    }


    //UPDATE STAFF ROLE
    @PutMapping("/update/role")
    public ResponseEntity<String> updateRole(@RequestBody Staff staffObject, @RequestHeader("Authorization") String token) {

        System.out.println("Staff role update started ==>");
        return staffServiceInterface.updateRole(staffObject, token);
    }

}
