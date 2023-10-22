package com.hospitalservice.mkobo.utility;

import com.hospitalservice.mkobo.dtos.JwtDecryptResponse;
import com.hospitalservice.mkobo.entities.Staff;
import com.hospitalservice.mkobo.enums.Roles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.Objects;

@Component
public class Reusable {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    public static String generateUUID() {

        return java.util.UUID.randomUUID().toString();
    }

    public boolean validateTokenAndUuid(String token, String dbUuid) {

        System.out.println("VALIDATING TOKEN AND UUID ===> ");
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(keyBytes)) // Use a SecretKey
                    .build()
                    .parseClaimsJws(token.substring(7))
                    .getBody();

            String tokenUuid = claims.get("uuid", String.class);

            // Compare object from JWT with the request body
            return tokenUuid.equals(dbUuid);
        } catch (Exception e) {
            System.out.println("Invalid token ==>" + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public ResponseEntity<JwtDecryptResponse> fetchTokenUuidAndEmail(String token) {

        System.out.println("FETCHING TOKEN VALUES ===> ");
        try {
            System.out.println("secret key: "+secretKey);
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(keyBytes)) // Use a SecretKey
                    .build()
                    .parseClaimsJws(token.substring(7))
                    .getBody();

            String tokenUuid = claims.get("uuid", String.class);
            String tokenEmail = claims.get("sub", String.class);

            JwtDecryptResponse jwtDecryptResponse = JwtDecryptResponse.builder()
                    .uuid(tokenUuid)
                    .email(tokenEmail)
                    .build();
            // Compare object from JWT with the request body
            return new ResponseEntity<>(jwtDecryptResponse, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Invalid token ==>" + e.getMessage());
            e.printStackTrace();
        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    public boolean validateHODRole(Staff staff) {

        return Objects.equals(staff.getRole(), Roles.HOD);
    }
}
