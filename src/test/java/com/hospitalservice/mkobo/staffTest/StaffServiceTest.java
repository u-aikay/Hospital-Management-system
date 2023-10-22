package com.hospitalservice.mkobo.staffTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import com.hospitalservice.mkobo.entities.Staff;
import com.hospitalservice.mkobo.repositories.StaffRepository;
import com.hospitalservice.mkobo.services.serviceImpl.StaffServiceImpl;
import com.hospitalservice.mkobo.utility.Reusable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;



public class StaffServiceTest {

    @InjectMocks
    private StaffServiceImpl staffService;

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private Reusable reusable;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToPassWhenRegistrationIsInitiatedByHOD() {
        Staff staffObject = new Staff();
        staffObject.setEmail("staff@example.com");
        staffObject.setPassword("password");

        String token = "mockToken";
        String uuid = "mockUuid";

        Staff registeredStaff = new Staff();
        registeredStaff.setEmail("staff@example.com");
        registeredStaff.setUuid(uuid);

        when(staffRepository.findByEmail(staffObject.getEmail())).thenReturn(Optional.of(registeredStaff));
        when(reusable.validateTokenAndUuid(token, uuid)).thenReturn(true);
        when(reusable.validateHODRole(registeredStaff)).thenReturn(true);

        // Mock the BCryptPasswordEncoder
        BCryptPasswordEncoder encoder = Mockito.mock(BCryptPasswordEncoder.class);
        when(encoder.encode(staffObject.getPassword())).thenReturn("hashedPassword");
        when(staffRepository.save(staffObject)).thenReturn(staffObject);

        ResponseEntity<String> response = staffService.registerStaff(staffObject, token);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Staff successfully registered", response.getBody());
    }

    @Test
    public void testRegistrationWithStaffDataMismatch() {
        Staff staffObject = new Staff();
        staffObject.setEmail("staff@example.com");
        staffObject.setPassword("password");

        String token = "mockToken";
        String uuid = "differentUuid";

        // Create a mock registered staff
        Staff registeredStaff = new Staff();
        registeredStaff.setEmail("staff@example.com");
        registeredStaff.setUuid(uuid);

        when(staffRepository.findByEmail(staffObject.getEmail())).thenReturn(Optional.of(registeredStaff));

        ResponseEntity<String> response = staffService.registerStaff(staffObject, token);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Staff data mismatch", response.getBody());
    }

    @Test
    public void testToFailWhenRegistrationIsInitiatedByStaffNotHOD() {
        Staff staffObject = new Staff();
        staffObject.setEmail("staff@example.com");
        staffObject.setPassword("password");

        String token = "mockToken";
        String uuid = "mockUuid";

        // Create a mock registered staff without HOD role
        Staff registeredStaff = new Staff();
        registeredStaff.setEmail("staff@example.com");
        registeredStaff.setUuid(uuid);

        when(staffRepository.findByEmail(staffObject.getEmail())).thenReturn(Optional.of(registeredStaff));
        when(reusable.validateTokenAndUuid(token, uuid)).thenReturn(true);
        when(reusable.validateHODRole(registeredStaff)).thenReturn(false);

        ResponseEntity<String> response = staffService.registerStaff(staffObject, token);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("unauthorised request", response.getBody());
    }

}

