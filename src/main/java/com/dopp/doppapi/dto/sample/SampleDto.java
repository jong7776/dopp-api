package com.dopp.doppapi.dto.sample;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SampleDto {
    private BigDecimal id;
    private String name;
    private String department;
}
