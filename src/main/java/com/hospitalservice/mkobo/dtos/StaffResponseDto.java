package com.hospitalservice.mkobo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class StaffResponseDto {

    private String message;
    private int code;

}
