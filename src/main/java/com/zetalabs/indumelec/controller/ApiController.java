package com.zetalabs.indumelec.controller;

import com.zetalabs.indumelec.model.Company;
import com.zetalabs.indumelec.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @Autowired
    private CompanyService companyService;

    @RequestMapping("/api/company/search")
    public ResponseEntity greeting(@RequestParam("taxId") String taxId) {
        Company company = companyService.getCompanyByTaxId(taxId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ResponseEntity responseEntity = new ResponseEntity<>(headers, HttpStatus.OK);

        if (company != null) {
            responseEntity = new ResponseEntity<>(company, headers, HttpStatus.OK);
        }

        return responseEntity;
    }
}
