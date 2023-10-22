package com.hospitalservice.mkobo.configs.securityConfigs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospitalservice.mkobo.dtos.AuthenticationRequest;
import com.hospitalservice.mkobo.dtos.AuthenticationResponse;
import com.hospitalservice.mkobo.entities.Staff;
import com.hospitalservice.mkobo.services.serviceInterface.StaffServiceInterface;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private final StaffServiceInterface staffServiceInterface;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            // Authentication failed, return a proper response
            return AuthenticationResponse.builder()
                    .accessToken(null)
                    .refreshToken(null)
                    .message(e.getMessage())
                    .build();
        }

        Optional<Staff> staff = staffServiceInterface.findByEmail(request.getEmail());

        String jwtToken = jwtService.generateToken(staff.get(), staff.get().getUuid());
        String refreshToken = jwtService.generateRefreshToken(staff.get());
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .message("access granted")
                .build();
    }


    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            Optional<Staff> staff = this.staffServiceInterface.findByEmail(userEmail);
            if (jwtService.isTokenValid(refreshToken, staff.get())) {
                String accessToken = jwtService.generateToken(staff.get(), staff.get().getUuid());
                AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

}
