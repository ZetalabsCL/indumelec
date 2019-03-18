package com.zetalabs.indumelec.controller;

import com.zetalabs.indumelec.model.Company;
import com.zetalabs.indumelec.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @Autowired
    private CompanyService companyService;

    @RequestMapping("/api/company/search")
    public Company greeting(@RequestParam("taxId") String taxId) {
        Company company = companyService.getCompanyByTaxId(taxId);

        if (company==null){
            company = new Company();
        }

        return company;
    }
}
