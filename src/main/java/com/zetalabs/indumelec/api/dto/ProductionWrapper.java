package com.zetalabs.indumelec.api.dto;

import com.zetalabs.indumelec.model.Quote;
import com.zetalabs.indumelec.model.types.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProductionWrapper extends AbstractWrapper {
    private Status status;
    private List<Quote> quoteList;
}