package com.zetalabs.indumelec.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QueryDates {
    private LocalDateTime startToday;
    private LocalDateTime startCurrentWeek;
    private LocalDateTime currentWeek;
    private LocalDateTime startPreviousWeek;
    private LocalDateTime previousWeek;
}
