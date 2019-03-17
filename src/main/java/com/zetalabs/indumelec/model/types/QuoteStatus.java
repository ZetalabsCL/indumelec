package com.zetalabs.indumelec.model.types;

import lombok.Getter;

@Getter
public enum QuoteStatus {
    NEW(0),
    REVIEW(1),
    APPROVED(2),
    REJECTED(3),
    CUT(4),
    PRODUCTION(5),
    BUILD(6),
    DELIVERY(7),
    COMPLETED(8);

    private Integer statusId;

    QuoteStatus(Integer statusId){
        this.statusId=statusId;
    }
}
