package com.zetalabs.indumelec.service;

import com.zetalabs.indumelec.model.Holiday;
import com.zetalabs.indumelec.repository.HolidayRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
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
