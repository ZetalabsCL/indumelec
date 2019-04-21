package com.zetalabs.indumelec.api.dto;

import lombok.Data;

@Data
public class QuoteWrapper extends AbstractWrapper {
    private Long quoteId;
    private String quoteCode;
    private String workOrder;
    private String status;
    private String company;
    private String entryDate;
    private String deliveryDate;
    private String reference;
    private String amount;
    private Integer daysLeft;
}
