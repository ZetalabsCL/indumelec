package com.zetalabs.indumelec.service;

import com.zetalabs.indumelec.model.Holiday;
import com.zetalabs.indumelec.repository.HolidayRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class HolidayServiceTest {
    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private HolidayService holidayService;

    @Test
    public void getHolidayList(){
        holidayService.getHolidayCalendar();

        List<Holiday> holidayList = holidayRepository.findAll();

        assertNotNull(holidayList);
        assertTrue(!holidayList.isEmpty());
    }
}
