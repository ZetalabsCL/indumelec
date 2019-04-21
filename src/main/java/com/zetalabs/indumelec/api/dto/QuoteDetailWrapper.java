package com.zetalabs.indumelec.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuoteDetailWrapper extends AbstractWrapper{
    private String description;
    private String measure;
    private BigDecimal quantity;
}
