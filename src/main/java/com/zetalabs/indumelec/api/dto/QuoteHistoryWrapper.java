package com.zetalabs.indumelec.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuoteHistoryWrapper extends AbstractWrapper {
    private String entryDate;
    private String description;
    private String comments;
    private String user;
}
