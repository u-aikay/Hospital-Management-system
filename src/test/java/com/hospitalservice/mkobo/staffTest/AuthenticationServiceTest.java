package com.hospitalservice.mkobo.staffTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import com.hospitalservice.mkobo.configs.securityConfigs.AuthenticationService;
import com.hospitalservice.mkobo.configs.securityConfigs.JwtService;
import com.hospitalservice.mkobo.dtos.AuthenticationRequest;
import com.hospitalservice.mkobo.dtos.AuthenticationResponse;
import com.hospitalservice.mkobo.entities.Staff;
import com.hospitalservice.mkobo.services.serviceInterface.StaffServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.util.Optional;



public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private StaffServiceInterface staffServiceInterface;
    @Mock
    private JwtService jwtService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    public void testAuthenticateWithValidCredentials() {
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "password");
        Staff staff = new Staff();
        staff.setUuid("mockUuid");

        // Mock the behavior of the AuthenticationManager
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())))
                .thenReturn(null);

        when(staffServiceInterface.findByEmail(request.getEmail())).thenReturn(Optional.of(staff));
        when(jwtService.generateToken(staff, staff.getUuid())).thenReturn("mockAccessToken");
        when(jwtService.generateRefreshToken(staff)).thenReturn("mockRefreshToken");

        AuthenticationResponse response = authenticationService.authenticate(request);

        // Verify that the response is as expected
        assertEquals("mockAccessToken", response.getAccessToken());
        assertEquals("mockRefreshToken", response.getRefreshToken());
        assertEquals("access granted", response.getMessage());
    }

    @Test
    public void testAuthenticateWithInvalidCredentials() {
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "invalidPassword");

        // Mock the behavior of the AuthenticationManager to throw a BadCredentialsException
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        AuthenticationResponse response = authenticationService.authenticate(request);

        // Verify that the response is as expected for invalid credentials
        assertEquals(null, response.getAccessToken());
        assertEquals(null, response.getRefreshToken());
        assertEquals("Invalid credentials", response.getMessage());
    }
}
