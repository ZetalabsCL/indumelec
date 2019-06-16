package com.zetalabs.indumelec.repository;

import com.zetalabs.indumelec.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday, LocalDate> {
    Holiday findByEntryDateAndNameAndType(LocalDate entryDate, String name, String type);

    List<Holiday> findHolidaysByEntryDateBetween(LocalDate startDate, LocalDate endDate);
}
