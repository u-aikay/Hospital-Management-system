package com.hospitalservice.mkobo.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtDecryptResponse {

    private String uuid;
    private String email;
}
