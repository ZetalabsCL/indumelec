package com.zetalabs.indumelec.service;

import com.zetalabs.indumelec.model.Holiday;
import com.zetalabs.indumelec.repository.HolidayRepository;
import com.zetalabs.indumelec.utils.IndumelecFormatter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
public class HolidayService {
    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Holiday> getHolidayList(LocalDate startDate, LocalDate endDate){
        return holidayRepository.findHolidaysByEntryDateBetween(startDate, endDate);
    }

    @Scheduled(cron = "0 0 6 1/1 * ? *")
    public void getHolidayCalendar() {
        String currentYear = String.valueOf(LocalDate.now().getYear());
        String uri = "https://apis.digital.gob.cl/fl/feriados/" + currentYear;

        String response = restTemplate.getForObject(uri, String.class);

        List<Holiday> holidayList = getHolidayList(new JSONArray(response));

        for (Holiday holiday : holidayList){
            Holiday existingHoliday = holidayRepository.findByEntryDateAndNameAndType(holiday.getEntryDate(), holiday.getName(), holiday.getType());

            if (existingHoliday==null) {
                holidayRepository.save(holiday);
            }
        }
    }

    private List<Holiday> getHolidayList(JSONArray array){
        List<Holiday> holidayList = new ArrayList<>();

        for (Object obj: array){
            JSONObject jsonObject = (JSONObject) obj;

            holidayList.add(getHoliday.apply(jsonObject));
        }

        return holidayList;
    }

    private Function<JSONObject, Holiday> getHoliday = (t) -> {
        Holiday holiday = new Holiday();
        holiday.setName(t.getString("nombre"));
        holiday.setType(t.getString("tipo"));
        holiday.setOptional(t.getInt("irrenunciable"));

        try {
            holiday.setEntryDate(LocalDate.parse(t.getString("fecha"), IndumelecFormatter.strDateFormat));
        } catch (DateTimeParseException ex){
            holiday.setEntryDate(null);
        }

        return holiday;
    };
}