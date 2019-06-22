package com.zetalabs.indumelec.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuoteWrapper extends AbstractWrapper {
    private Long quoteId;
    private String quoteCode;
    private String workOrder;
    private String status;
    private String company;
    private String contact;
    private String phone;
    private String entryDate;
    private String deliveryDate;
    private String reference;
    private String amount;
    private Integer daysLeft;

    private List<QuoteDetailWrapper> details;
}
