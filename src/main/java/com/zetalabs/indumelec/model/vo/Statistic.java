package com.zetalabs.indumelec.model.vo;

import lombok.Data;

@Data
public class Statistic {
    private Double reviewed;
    private Double reviewedLastMonth;
    private Double progress;
    private Double progressLastMonth;
    private Double closed;
    private Double closedLastMonth;

    private Long total;
    private Long inReview;
    private Long inProgress;
    private Long finished;
}
