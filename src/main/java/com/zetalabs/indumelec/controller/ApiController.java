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

import java.util.List;

@RestController
public class ApiController {
    @Autowired
    private CompanyService companyService;

    @RequestMapping("/api/company/search")
    public ResponseEntity companySearch(@RequestParam("taxId") String taxId) {
        ResponseEntity responseEntity = getResponse();
        Company company = companyService.getCompanyByTaxId(taxId);

        if (company != null) {
            responseEntity = new ResponseEntity<>(company, responseEntity.getHeaders(), HttpStatus.OK);
        }

        return responseEntity;
    }

    @RequestMapping("/api/company/list")
    public ResponseEntity companyList() {
        ResponseEntity responseEntity = getResponse();
        List<Company> companyList = companyService.getCompanyList();

        if (companyList != null) {
            responseEntity = new ResponseEntity<>(companyList, responseEntity.getHeaders(), HttpStatus.OK);
        }

        return responseEntity;
    }

    private ResponseEntity getResponse(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ResponseEntity responseEntity = new ResponseEntity<>(headers, HttpStatus.OK);

        return responseEntity;
    }
}
