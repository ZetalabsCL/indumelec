package com.zetalabs.indumelec.controller;

import com.zetalabs.indumelec.service.ReportService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService){
        this.reportService = reportService;
    }

    @RequestMapping(value={"/report/dashboard"}, method = RequestMethod.GET)
    public ModelAndView dashboard(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("report/dashboard");

        return modelAndView;
    }

    @RequestMapping(value={"/report/download"}, method = RequestMethod.POST)
    public ResponseEntity<ByteArrayResource> download(@RequestParam("deliveryDateFrom") String from,
                                                      @RequestParam("deliveryDateTo") String to,
                                                      @RequestParam("priorityType") String priority){
        byte[] data = reportService.getReport(from, to, priority);
        ByteArrayResource resource = new ByteArrayResource(data);
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyy_HHmmss"));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\"reporte_" + currentDateTime + ".xlsx\"" )
                .contentType(MediaType.APPLICATION_PDF).contentLength(data.length)
                .body(resource);
    }
}
